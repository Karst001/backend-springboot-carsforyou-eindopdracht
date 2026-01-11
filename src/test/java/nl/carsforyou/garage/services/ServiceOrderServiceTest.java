//this unit test will have 7 test routines
//and happy-flow, covers not found, has parts (throws exception), vehicle exists (throws exception), ok on delete

package nl.carsforyou.garage.services;

import nl.carsforyou.garage.dtos.ServiceOrder.ServiceOrderRequestDto;
import nl.carsforyou.garage.dtos.ServiceOrder.ServiceOrderResponseDto;
import nl.carsforyou.garage.entities.ServiceOrderEntity;
import nl.carsforyou.garage.mappers.ServiceOrderDTOMapper;
import nl.carsforyou.garage.repositories.ServiceOrderPartRepository;
import nl.carsforyou.garage.repositories.ServiceOrderRepository;
import nl.carsforyou.garage.repositories.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ServiceOrderServiceTest {
    @Mock
    ServiceOrderRepository serviceOrderRepository;

    @Mock
    ServiceOrderDTOMapper serviceOrderDTOMapper;

    @Mock
    ServiceOrderPartRepository serviceOrderPartRepository;

    @Mock
    VehicleRepository vehicleRepository;

    @InjectMocks
    ServiceOrderService serviceOrderService;

    //test #1, test all Appointments, easy happy-flow
    @Test
    void getAllServiceOrders_returnsMappedList() {
        //Arrange
        var entities = List.of(new ServiceOrderEntity());
        var dtos = List.of(new ServiceOrderResponseDto());

        when(serviceOrderRepository.findAll()).thenReturn(entities);
        when(serviceOrderDTOMapper.mapToDtoList(entities)).thenReturn(dtos);

        //Act
        var result = serviceOrderService.getAllServiceOrders();

        //Assert
        assertSame(dtos, result);
        verify(serviceOrderRepository).findAll();
        verify(serviceOrderDTOMapper).mapToDtoList(entities);
    }


    //test #2, test service order, change findById(1L) to findById(10L) and test will fail
    @Test
    void getServiceOrderById_whenFound_returnsMappedDto() {
        //Arrange
        var entity = new ServiceOrderEntity();
        var mapped = new ServiceOrderResponseDto();

        when(serviceOrderRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(serviceOrderDTOMapper.mapToDto(entity)).thenReturn(mapped);

        //Act
        var result = serviceOrderService.getServiceOrderById(1L);

        //Assert
        assertSame(mapped, result);
        verify(serviceOrderRepository).findById(1L);
        verify(serviceOrderDTOMapper).mapToDto(entity);
    }



    //test #3, test create service order, change setVehicleId(3L) to setVehicleId(1L) and test will fail
    @Test
    void createServiceOrder_happyFlow_setsVehicle_saves_andReturnsMappedDto() {
        //Arrange
        var dto = new ServiceOrderRequestDto();
        dto.setVehicleId(3L);

        var entity = new ServiceOrderEntity();
        var vehicle = new nl.carsforyou.garage.entities.VehicleEntity();

        var saved = new ServiceOrderEntity();
        var mapped = new ServiceOrderResponseDto();

        when(serviceOrderDTOMapper.mapToEntity(dto)).thenReturn(entity);
        when(vehicleRepository.findById(3L)).thenReturn(Optional.of(vehicle));
        when(serviceOrderRepository.save(entity)).thenReturn(saved);
        when(serviceOrderDTOMapper.mapToDto(saved)).thenReturn(mapped);

        //Act
        var result = serviceOrderService.createServiceOrder(dto);

        //Assert
        assertSame(mapped, result);
        assertSame(vehicle, entity.getVehicle());

        verify(serviceOrderDTOMapper).mapToEntity(dto);
        verify(vehicleRepository).findById(3L);
        verify(serviceOrderRepository).save(entity);
        verify(serviceOrderDTOMapper).mapToDto(saved);
    }



    //test #4, test update service order, change setVehicleId(3L) to setVehicleId(1L) and test will fail
    @Test
    void updateServiceOrder_happyFlow_setsFields_setsVehicle_saves_andReturnsMappedDto() {
        //Arrange
        var dto = new ServiceOrderRequestDto();
        dto.setVehicleId(1L);
        var completed = LocalDateTime.now().plusHours(1); // date must be after now() to pass DateValidationUtil
        dto.setServiceCompletedDate(completed);

        var existing = new ServiceOrderEntity();
        var vehicle = new nl.carsforyou.garage.entities.VehicleEntity();

        var saved = new ServiceOrderEntity();
        var mapped = new ServiceOrderResponseDto();

        when(serviceOrderRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(serviceOrderRepository.save(existing)).thenReturn(saved);
        when(serviceOrderDTOMapper.mapToDto(saved)).thenReturn(mapped);

        //Act
        var result = serviceOrderService.updateServiceOrder(5L, dto);

        //Assert
        assertSame(mapped, result);

        assertEquals(completed, existing.getServiceCompletedDate());
        assertEquals(1L, existing.getVehicleId());
        assertSame(vehicle, existing.getVehicle());

        verify(serviceOrderRepository).findById(5L);
        verify(vehicleRepository).findById(1L);
        verify(serviceOrderRepository).save(existing);
        verify(serviceOrderDTOMapper).mapToDto(saved);
    }



    //test #5, test delete service order, change findById(10L) to findById(1L) and test will fail
    @Test
    void deleteServiceOrder_whenHasParts_throws400_andDoesNotDelete() {
        //Arrange
        var existing = new ServiceOrderEntity();
        when(serviceOrderRepository.findById(10L)).thenReturn(Optional.of(existing));
        when(serviceOrderPartRepository.existsByServiceOrder_ServiceOrderId(10L)).thenReturn(true);

        //Act
        var ex = assertThrows(ResponseStatusException.class,
                () -> serviceOrderService.deleteServiceOrder(10L));

        //Assert
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("because it has parts"));
        verify(serviceOrderRepository, never()).delete(any());
    }


    //test #6, test delete service order, change findById(10L) to findById(1L) and test will fail
    @Test
    void deleteServiceOrder_whenNoPartsAndNoVehicleRelation_deletes() {
        //Arrange
        var existing = new ServiceOrderEntity();
        when(serviceOrderRepository.findById(10L)).thenReturn(Optional.of(existing));
        when(serviceOrderPartRepository.existsByServiceOrder_ServiceOrderId(10L)).thenReturn(false);
        when(serviceOrderRepository.existsByVehicle_VehicleId(10L)).thenReturn(false);

        //Act
        serviceOrderService.deleteServiceOrder(10L);

        //Assert
        verify(serviceOrderRepository).delete(existing);
    }


    //test #7, test delete service order, change findById(10L) to findById(1L) and test will fail
    @Test
    void deleteServiceOrder_whenVehicleRelationExists_throws400_andDoesNotDelete() {
        //Arrange
        var existing = new ServiceOrderEntity();
        when(serviceOrderRepository.findById(10L)).thenReturn(Optional.of(existing));
        when(serviceOrderPartRepository.existsByServiceOrder_ServiceOrderId(10L)).thenReturn(false);
        when(serviceOrderRepository.existsByVehicle_VehicleId(10L)).thenReturn(true);

        //Act
        var ex = assertThrows(ResponseStatusException.class,
                () -> serviceOrderService.deleteServiceOrder(10L));

        //Assert
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("because service orders exist"));
        verify(serviceOrderRepository, never()).delete(any());
    }
}
