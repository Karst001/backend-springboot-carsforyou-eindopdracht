package nl.carsforyou.garage.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import nl.carsforyou.garage.dtos.appointment.AppointmentRequestDto;
import nl.carsforyou.garage.dtos.appointment.AppointmentResponseDto;
import nl.carsforyou.garage.helpers.UrlHelper;
import nl.carsforyou.garage.services.AppointmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AppointmentControllerNotFoundIT {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean AppointmentService appointmentService;
    @MockitoBean UrlHelper urlHelper;


    @Test
    void getAllAppointments_returns200_andListWithFields() throws Exception {
        //arrange
        var apptOne = dto(1L, LocalDateTime.of(2026, 1, 20, 10, 0), "APK", null, null, 10L, 2L);
        var apptTwo = dto(2L, LocalDateTime.of(2026, 1, 22, 9, 30), "Onderhoud", null, null, 11L, null);

        when(appointmentService.getAllAppointments()).thenReturn(List.of(apptOne, apptTwo));

        //act and assert
        mockMvc.perform(get("/appointments").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))

                .andExpect(jsonPath("$[0].appointmentId").value(1))
                .andExpect(jsonPath("$[0].reasonForVisit").value("APK"))
                .andExpect(jsonPath("$[0].vehicleId").value(10))
                .andExpect(jsonPath("$[0].createdByUserId").value(2))
                .andExpect(jsonPath("$[0].appointmentDate").value("2026-01-20T10:00:00"))

                .andExpect(jsonPath("$[1].appointmentId").value(2))
                .andExpect(jsonPath("$[1].reasonForVisit").value("Onderhoud"))
                .andExpect(jsonPath("$[1].vehicleId").value(11))
                .andExpect(jsonPath("$[1].createdByUserId").doesNotExist()) // null usually omitted
                .andExpect(jsonPath("$[1].appointmentDate").value("2026-01-22T09:30:00"));

        verify(appointmentService).getAllAppointments();
    }

    //helper
    private AppointmentResponseDto dto(Long id, LocalDateTime date, String reason, LocalDateTime completed,
                                       LocalDateTime cancelled, Long vehicleId, Long createdByUserId)
    {
        AppointmentResponseDto dto = new AppointmentResponseDto();
        dto.setAppointmentId(id);
        dto.setAppointmentDate(date);
        dto.setReasonForVisit(reason);
        dto.setCompletedDate(completed);
        dto.setCancelledDate(cancelled);
        dto.setVehicleId(vehicleId);
        dto.setCreatedByUserId(createdByUserId);
        return dto;
    }

    @Test
    void getAppointmentById_whenNotFound_returns404_andErrorJson() throws Exception {
        //arrange
        long nonExistingId = 999L;

        when(appointmentService.getAppointmentById(nonExistingId))
            .thenThrow(new EntityNotFoundException("Appointment with Id " + nonExistingId + " was not found"));

        //act and assert
        mockMvc.perform(get("/appointments/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.error").value("Appointment with Id " + nonExistingId + " was not found"));

        verify(appointmentService).getAppointmentById(nonExistingId);
    }


    @Test
    void createAppointment_returns201_Location_andBodyFields() throws Exception {
        //arrange
        AppointmentRequestDto appointment = new AppointmentRequestDto();
        appointment.setAppointmentDate(LocalDateTime.of(2026, 1, 25, 14, 0));
        appointment.setReasonForVisit("Nieuwe afspraak");
        appointment.setVehicleId(10L);
        appointment.setCreatedByUserId(null);
        appointment.setCompletedDate(null);

        var created = dto(
            123L,
            LocalDateTime.of(2026, 1, 25, 14, 0),
            "Nieuwe afspraak",
            null,
            null,
            10L,
            null
        );

        when(appointmentService.createAppointment(any(AppointmentRequestDto.class))).thenReturn(created);
        when(urlHelper.getCurrentUrlWithId(123L)).thenReturn(URI.create("http://localhost/appointments/123"));

        //act and assert
        mockMvc.perform(post("/appointments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(appointment))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "http://localhost/appointments/123"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.appointmentId").value(123))
            .andExpect(jsonPath("$.reasonForVisit").value("Nieuwe afspraak"))
            .andExpect(jsonPath("$.vehicleId").value(10))
            .andExpect(jsonPath("$.appointmentDate").value("2026-01-25T14:00:00"))
            .andExpect(jsonPath("$.completedDate").doesNotExist())
            .andExpect(jsonPath("$.cancelledDate").doesNotExist())
            .andExpect(jsonPath("$.createdByUserId").doesNotExist());

        verify(appointmentService).createAppointment(any(AppointmentRequestDto.class));
        verify(urlHelper).getCurrentUrlWithId(123L);
    }


    @Test
    void updateAppointment_returns200_andUpdatedFields() throws Exception {
        //arrange
        long id = 5L;

        AppointmentRequestDto appointment = new AppointmentRequestDto();
        appointment.setAppointmentDate(LocalDateTime.of(2026, 1, 30, 9, 0));
        appointment.setReasonForVisit("Updated reason");
        appointment.setVehicleId(10L);
        appointment.setCompletedDate(LocalDateTime.of(2026, 1, 30, 10, 0));

        var updated = dto(
            id,
            LocalDateTime.of(2026, 1, 30, 9, 0),
            "Updated reason",
            LocalDateTime.of(2026, 1, 30, 10, 0),
            null,
            10L,
            null
        );

        when(appointmentService.updateAppointment(eq(id), any(AppointmentRequestDto.class))).thenReturn(updated);

        //act and assert
        mockMvc.perform(put("/appointments/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(appointment))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.appointmentId").value(5))
            .andExpect(jsonPath("$.reasonForVisit").value("Updated reason"))
            .andExpect(jsonPath("$.vehicleId").value(10))
            .andExpect(jsonPath("$.appointmentDate").value("2026-01-30T09:00:00"))
            .andExpect(jsonPath("$.completedDate").value("2026-01-30T10:00:00"));

        verify(appointmentService).updateAppointment(eq(id), any(AppointmentRequestDto.class));
    }


    @Test
    void cancelAppointment_returns200_andCancelledDatePresent() throws Exception {
        //arrange
        long id = 7L;

        var cancelled = dto(
            id,
            LocalDateTime.of(2026, 2, 1, 12, 0),
            "To cancel",
            null,
            LocalDateTime.of(2026, 1, 10, 15, 0),
            10L,
            null
        );

        when(appointmentService.cancelAppointment(id)).thenReturn(cancelled);

        //act and assert
        mockMvc.perform(patch("/appointments/{id}", id).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.appointmentId").value(7))
            .andExpect(jsonPath("$.reasonForVisit").value("To cancel"))
            .andExpect(jsonPath("$.vehicleId").value(10))
            .andExpect(jsonPath("$.cancelledDate").value("2026-01-10T15:00:00"));

        verify(appointmentService).cancelAppointment(id);
    }


    @Test
    void deleteAppointment_returns204() throws Exception {
        //arrange
        long id = 8L;
        doNothing().when(appointmentService).deleteAppointment(id);

        //act and assert
        mockMvc.perform(delete("/appointments/{id}", id))
                .andExpect(status().isNoContent());

        verify(appointmentService).deleteAppointment(id);
    }
}

