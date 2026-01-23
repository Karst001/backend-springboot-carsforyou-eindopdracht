package nl.carsforyou.garage.services;

import nl.carsforyou.garage.dtos.appointment.AppointmentRequestDto;
import nl.carsforyou.garage.dtos.appointment.AppointmentResponseDto;
import nl.carsforyou.garage.entities.AppointmentEntity;
import nl.carsforyou.garage.entities.VehicleEntity;
import nl.carsforyou.garage.mappers.AppointmentDTOMapper;
import nl.carsforyou.garage.repositories.AppointmentRepository;
import nl.carsforyou.garage.repositories.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {
    @Mock
    AppointmentRepository appointmentRepository;

    @Mock
    AppointmentDTOMapper appointmentDTOMapper;

    @Mock
    VehicleRepository vehicleRepository;

    @InjectMocks
    AppointmentService appointmentService;

    //test #1, test all Appointments, easy happy-flow
    @Test
    void getAllAppointments_returnsMappedList() {
        //Arrange
        var entities = List.of(new AppointmentEntity(), new AppointmentEntity());
        var dtos = List.of(new AppointmentResponseDto(), new AppointmentResponseDto());

        when(appointmentRepository.findAll()).thenReturn(entities);
        when(appointmentDTOMapper.mapToDtoList(entities)).thenReturn(dtos);

        //Act
        var result = appointmentService.getAllAppointments();

        //Assert
        assertSame(dtos, result);
        verify(appointmentRepository).findAll();
        verify(appointmentDTOMapper).mapToDtoList(entities);
    }


    //test #2, test appointments by Id, change findById(3L) to 1L and it fails
    @Test
    void getAppointmentById_whenFound_returnsDto() {
        //Arrange
        var entity = new AppointmentEntity();
        var dto = new AppointmentResponseDto();
        dto.setAppointmentId(1L);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(appointmentDTOMapper.mapToDto(entity)).thenReturn(dto);

        //Act
        var result = appointmentService.getAppointmentById(1L);

        //Assert
        assertEquals(1L, result.getAppointmentId());
        verify(appointmentRepository).findById(1L);
        verify(appointmentDTOMapper).mapToDto(entity);
    }


    //test #3, test appointments by Id that does not exists, change findById(3L) to 1L and it fails
    @Test
    void getAppointmentById_whenMissing_throwsEntityNotFound() {
        // Arrange
        long missingId = 3L;
        when(appointmentRepository.findById(missingId)).thenReturn(Optional.empty());

        // Act
        var ex = assertThrows(EntityNotFoundException.class,
                () -> appointmentService.getAppointmentById(missingId));

        // Assert
        assertTrue(ex.getMessage().contains("Appointment with Id " + missingId));
        verify(appointmentRepository).findById(missingId);
        verifyNoInteractions(appointmentDTOMapper);
    }


    //test #4, test create appointments, change  dto.setVehicleId(1L); to 2L and test fails
    @Test
    void createAppointment_whenValid_savesAndReturnsDto_andSetsCompletedDateNull() {
        //Arrange
        var dto = new AppointmentRequestDto();
        dto.setAppointmentDate(LocalDateTime.now().plusDays(1));
        dto.setReasonForVisit("APK");
        dto.setVehicleId(1L);
        dto.setCompletedDate(LocalDateTime.now().plusDays(2));

        var entityFromMapper = new AppointmentEntity();
        entityFromMapper.setCompletedDate(LocalDateTime.now()); // this is to prove the service overwrites to null

        var vehicle = new VehicleEntity();
        var saved = new AppointmentEntity();
        var savedDto = new AppointmentResponseDto();
        savedDto.setAppointmentId(2L);

        when(appointmentDTOMapper.mapToEntity(dto)).thenReturn(entityFromMapper);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(appointmentRepository.save(entityFromMapper)).thenReturn(saved);
        when(appointmentDTOMapper.mapToDto(saved)).thenReturn(savedDto);

        //Act
        var result = appointmentService.createAppointment(dto);

        //Assert
        assertEquals(2L, result.getAppointmentId());
        assertNull(entityFromMapper.getCompletedDate(), "completedDate must be null at creation");
        verify(vehicleRepository).findById(1L);
        verify(appointmentRepository).save(entityFromMapper);
        verify(appointmentDTOMapper).mapToDto(saved);
    }


    //test #5, test create appointments, change  .minusDays(1 to .minusDays(0)) and test will fail
    @Test
    void createAppointment_whenAppointmentDateInPast_throws400_andDoesNotSave() {
        //Arrange
        var dto = new AppointmentRequestDto();
        dto.setAppointmentDate(LocalDateTime.now().minusDays(1)); // this will trigger a DateValidationUtil.validateNotInPast
        dto.setVehicleId(1L);

        when(appointmentDTOMapper.mapToEntity(dto)).thenReturn(new AppointmentEntity());

        //Act
        var ex = assertThrows(ResponseStatusException.class,
                () -> appointmentService.createAppointment(dto));

        //Assert
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        verify(appointmentRepository, never()).save(any());
        verify(vehicleRepository, never()).findById(any());
    }


    //test #6, test not existing  appointment, change findById(55L)) to findById(1L)) and test will fail
    @Test
    void updateAppointment_whenMissing_throws404() {
        //Arrange
        var dto = new AppointmentRequestDto();
        dto.setAppointmentDate(LocalDateTime.now().plusDays(1));
        dto.setVehicleId(1L);

        when(appointmentRepository.findById(55L)).thenReturn(Optional.empty());

        //Act
        var ex = assertThrows(ResponseStatusException.class,
                () -> appointmentService.updateAppointment(55L, dto));

        //Assert
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Appointment with Id 55"));
        verify(appointmentRepository).findById(55L);
        verify(appointmentRepository, never()).save(any());
    }


    //test #6a, below test will check 'DateValidationUtil.validateDateOrder'
    @Test
    void updateAppointment_whenCompletedBeforeAppointment_throws400() {
        //arrange
        long id = 5L;

        AppointmentEntity existing = new AppointmentEntity();
        existing.setAppointmentId(id);

        LocalDateTime apptDate = LocalDateTime.of(2026, 2, 1, 10, 0);
        LocalDateTime completed = apptDate.minusHours(1);

        AppointmentRequestDto dto = new AppointmentRequestDto();
        dto.setAppointmentDate(apptDate);
        dto.setCompletedDate(completed);
        dto.setReasonForVisit("Updated");
        dto.setVehicleId(10L);

        when(appointmentRepository.findById(id)).thenReturn(Optional.of(existing));

        //act
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> appointmentService.updateAppointment(id, dto));

        //assert
        if (ex instanceof ResponseStatusException exception) {
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
            assertTrue(exception.getReason().contains("appointmentDate") || exception.getReason().contains("completedDate"));
        }

        verify(appointmentRepository).findById(id);
        verifyNoInteractions(vehicleRepository);
        verify(appointmentRepository, never()).save(any());
        verifyNoInteractions(appointmentDTOMapper);
    }


    //test #6b, below tests 'existing.set.....'
    @Test
    void updateAppointment_whenVehicleMissing_throws404() {
        //arrange
        long id = 5L;
        long missingVehicleId = 999L;

        AppointmentEntity existing = new AppointmentEntity();
        existing.setAppointmentId(id);

        AppointmentRequestDto dto = new AppointmentRequestDto();
        dto.setAppointmentDate(LocalDateTime.of(2026, 2, 1, 10, 0));
        dto.setCompletedDate(null);
        dto.setReasonForVisit("Updated");
        dto.setVehicleId(missingVehicleId);

        when(appointmentRepository.findById(id)).thenReturn(Optional.of(existing));
        when(vehicleRepository.findById(missingVehicleId)).thenReturn(Optional.empty());

        //act
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
            () -> appointmentService.updateAppointment(id, dto));

        //assert
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertTrue(ex.getReason().toLowerCase().contains("vehicle"));

        verify(appointmentRepository).findById(id);
        verify(vehicleRepository).findById(missingVehicleId);
        verify(appointmentRepository, never()).save(any());
        verifyNoInteractions(appointmentDTOMapper);
    }


    //test #6c, below the save methods
    @Test
    void updateAppointment_happyPath_updatesFields_setsVehicle_saves_andMaps() {
        //arrange
        long id = 5L;
        long vehicleId = 10L;

        AppointmentEntity existing = new AppointmentEntity();
        existing.setAppointmentId(id);
        existing.setReasonForVisit("Old appointment");
        existing.setAppointmentDate(LocalDateTime.of(2026, 1, 1, 9, 0));

        VehicleEntity vehicle = new VehicleEntity();
        vehicle.setVehicleId(vehicleId);

        LocalDateTime newDate = LocalDateTime.of(2026, 2, 1, 10, 0);
        LocalDateTime completed = newDate.plusHours(1);

        AppointmentRequestDto dto = new AppointmentRequestDto();
        dto.setAppointmentDate(newDate);
        dto.setReasonForVisit("Updated appointment");
        dto.setCompletedDate(completed);
        dto.setVehicleId(vehicleId);

        AppointmentEntity saved = new AppointmentEntity();
        saved.setAppointmentId(id);
        saved.setAppointmentDate(newDate);
        saved.setReasonForVisit("Updated appointment");
        saved.setCompletedDate(completed);
        saved.setVehicle(vehicle);
        saved.setVehicleId(vehicleId);

        AppointmentResponseDto mapped = new AppointmentResponseDto();
        mapped.setAppointmentId(id);

        when(appointmentRepository.findById(id)).thenReturn(Optional.of(existing));
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));
        when(appointmentRepository.save(any(AppointmentEntity.class))).thenReturn(saved);
        when(appointmentDTOMapper.mapToDto(saved)).thenReturn(mapped);

        //act
        AppointmentResponseDto result = appointmentService.updateAppointment(id, dto);

        //assert
        assertNotNull(result);
        assertEquals(id, result.getAppointmentId());

        assertEquals(newDate, existing.getAppointmentDate());
        assertEquals("Updated appointment", existing.getReasonForVisit());
        assertEquals(completed, existing.getCompletedDate());
        assertEquals(vehicleId, existing.getVehicleId());
        assertEquals(vehicle, existing.getVehicle());

        verify(appointmentRepository).findById(id);
        verify(vehicleRepository).findById(vehicleId);
        verify(appointmentRepository).save(existing);
        verify(appointmentDTOMapper).mapToDto(saved);
    }


    //test #7, test delete appointment, change findById(7L)) to findById(1L)) and test will fail
    @Test
    void deleteAppointment_whenFound_deletesEntity() {
        //Arrange
        var entity = new AppointmentEntity();
        when(appointmentRepository.findById(7L)).thenReturn(Optional.of(entity));

        //Act
        appointmentService.deleteAppointment(7L);

        //Assert
        verify(appointmentRepository).delete(entity);
    }


    //test #8, test cancel appointment, change setAppointmentId(9L) to setAppointmentId(1L) and test will fail
    @Test
    void cancelAppointment_whenFound_setsCancelledDate_savesAndReturnsDto() {
        //Arrange
        var entity = new AppointmentEntity();
        entity.setCancelledDate(null);

        var savedDto = new AppointmentResponseDto();
        savedDto.setAppointmentId(9L);

        when(appointmentRepository.findById(9L)).thenReturn(Optional.of(entity));
        when(appointmentRepository.save(entity)).thenReturn(entity);
        when(appointmentDTOMapper.mapToDto(entity)).thenReturn(savedDto);

        //Act
        var result = appointmentService.cancelAppointment(9L);

        //Assert
        assertEquals(9L, result.getAppointmentId());
        assertNotNull(entity.getCancelledDate(), "cancelledDate should be set");
        verify(appointmentRepository).save(entity);
    }
}
