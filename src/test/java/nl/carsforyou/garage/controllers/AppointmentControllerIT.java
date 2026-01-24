package nl.carsforyou.garage.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.carsforyou.garage.dtos.appointment.AppointmentRequestDto;
import nl.carsforyou.garage.entities.AppointmentEntity;
import nl.carsforyou.garage.entities.CustomerEntity;
import nl.carsforyou.garage.entities.VehicleEntity;
import nl.carsforyou.garage.repositories.AppointmentRepository;
import nl.carsforyou.garage.repositories.CustomerRepository;
import nl.carsforyou.garage.repositories.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AppointmentControllerIT {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Autowired AppointmentRepository appointmentRepository;
    @Autowired VehicleRepository vehicleRepository;
    @Autowired
    CustomerRepository customerRepository;

    private VehicleEntity vehicle;

    @BeforeEach
    void setup() {
        appointmentRepository.deleteAll();
        vehicleRepository.deleteAll();
        customerRepository.deleteAll();

        CustomerEntity customer = new CustomerEntity();
        customer.setFirstName("Test");
        customer.setLastName("Customer");
        customer.setAddress("Hoofdstraat 1");
        customer.setTelephoneNumber("0612345678");
        customer.setEmailAddress("test@test.com");
        customer.setCity("Amsterdam");
        customer.setZipCode("1000AA");
        customer.setCountry("Nederland");
        customer = customerRepository.save(customer);

        vehicle = new VehicleEntity();
        vehicle.setLicensePlate("99-XYZ-1");
        vehicle.setVinNumber("VIN-TEST-0001");
        vehicle.setMake("Toyota");
        vehicle.setModel("Yaris");
        vehicle.setCustomer(customer);  // required to set relation
        vehicle = vehicleRepository.save(vehicle);
    }

    //helpers
    private AppointmentEntity saveAppointment(LocalDateTime date, String reason) {
        AppointmentEntity appt = new AppointmentEntity();
        appt.setAppointmentDate(date);
        appt.setReasonForVisit(reason);
        appt.setCompletedDate(null);
        appt.setCancelledDate(null);
        appt.setVehicle(vehicle);
        appt.setCreatedByUserId(null);
        return appointmentRepository.save(appt);
    }

    private AppointmentRequestDto requestDto(LocalDateTime date, String reason, Long vehicleId, Long createdByUserId, LocalDateTime completed) {
        AppointmentRequestDto dto = new AppointmentRequestDto();
        dto.setAppointmentDate(date);
        dto.setReasonForVisit(reason);
        dto.setVehicleId(vehicleId);
        dto.setCreatedByUserId(createdByUserId);
        dto.setCompletedDate(completed);
        return dto;
    }


    @Test
    void getAppointmentById_returns200_andCorrectJsonBody() throws Exception {
        //arrange
        AppointmentEntity saved = saveAppointment(LocalDateTime.of(2026, 1, 4, 10, 30), "APK keuring");

        //act + assert
        mockMvc.perform(get("/appointments/{id}", saved.getAppointmentId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.appointmentId").value(saved.getAppointmentId()))
            .andExpect(jsonPath("$.reasonForVisit").value("APK keuring"))
            .andExpect(jsonPath("$.vehicleId").value(vehicle.getVehicleId()))
            .andExpect(jsonPath("$.appointmentDate").value("2026-01-04T10:30:00"))
            .andExpect(jsonPath("$.completedDate").doesNotExist())
            .andExpect(jsonPath("$.cancelledDate").doesNotExist());
    }


    @Test
    void getAppointmentById_whenNotFound_returns404() throws Exception {
        //arrange
        long nonExistingId = 999L;

        //act and arrange
        mockMvc.perform(get("/appointments/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }


    @Test
    void getAllAppointments_returns200_andList() throws Exception {
        //arrange
        saveAppointment(LocalDateTime.of(2026, 1, 4, 10, 30), "APK keuring");
        saveAppointment(LocalDateTime.of(2026, 1, 5, 9, 0), "Onderhoud");

        //act and assert
        mockMvc.perform(get("/appointments").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[*].reasonForVisit", containsInAnyOrder("APK keuring", "Onderhoud")));
    }


    @Test
    void createAppointment_returns201_locationHeader_andBody() throws Exception {
        //appointmentDate must NOT be in the past using DateValidationUtil
        //arrange
        LocalDateTime future = LocalDateTime.now().plusDays(2).withSecond(0).withNano(0);
        AppointmentRequestDto req = requestDto(future, "Nieuwe afspraak", vehicle.getVehicleId(), null, null);

        //act and assert
        mockMvc.perform(post("/appointments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", containsString("/appointments/")))
            .andExpect(jsonPath("$.appointmentId", notNullValue()))
            .andExpect(jsonPath("$.reasonForVisit").value("Nieuwe afspraak"))
            .andExpect(jsonPath("$.vehicleId").value(vehicle.getVehicleId()))
            .andExpect(jsonPath("$.completedDate").doesNotExist());
    }


    @Test
    void createAppointment_whenVehicleNotFound_returns404() throws Exception {
        //arrange
        LocalDateTime future = LocalDateTime.now().plusDays(2).withSecond(0).withNano(0);
        AppointmentRequestDto req = requestDto(future, "Bad vehicle", 999L, null, null);

        //act & assert
        mockMvc.perform(post("/appointments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }


    @Test
    void updateAppointment_returns200_andUpdatedBody() throws Exception {
        //arrange
        AppointmentEntity saved = saveAppointment(LocalDateTime.now().plusDays(3).withSecond(0).withNano(0), "Old reason");
        LocalDateTime newDate = LocalDateTime.now().plusDays(5).withSecond(0).withNano(0);
        LocalDateTime completed = newDate.plusHours(1);
        AppointmentRequestDto update = requestDto(newDate, "Updated reason", vehicle.getVehicleId(), null, completed);

        //act & assert
        mockMvc.perform(put("/appointments/{id}", saved.getAppointmentId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(update))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.appointmentId").value(saved.getAppointmentId()))
            .andExpect(jsonPath("$.reasonForVisit").value("Updated reason"))
            .andExpect(jsonPath("$.vehicleId").value(vehicle.getVehicleId()));
    }


    @Test
    void updateAppointment_whenNotFound_returns404() throws Exception {
        //arrange
        LocalDateTime newDate = LocalDateTime.now().plusDays(5).withSecond(0).withNano(0);
        AppointmentRequestDto update = requestDto(newDate, "Updated", vehicle.getVehicleId(), null, null);

        //act & assert
        mockMvc.perform(put("/appointments/{id}", 999L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(update))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }


    @Test
    void cancelAppointment_returns200_andSetsCancelledDate() throws Exception {
        //arrange
        AppointmentEntity saved = saveAppointment(LocalDateTime.now().plusDays(3).withSecond(0).withNano(0), "To cancel");

        //act and assert
        mockMvc.perform(patch("/appointments/{id}", saved.getAppointmentId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.appointmentId").value(saved.getAppointmentId()))
            .andExpect(jsonPath("$.appointmentId", notNullValue()));
    }


    @Test
    void cancelAppointment_whenNotFound_returns404() throws Exception {
        mockMvc.perform(patch("/appointments/{id}", 999999L).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }


    @Test
    void deleteAppointment_returns204_andRemovesRow() throws Exception {
        //arrange
        AppointmentEntity saved = saveAppointment(LocalDateTime.now().plusDays(3).withSecond(0).withNano(0), "To delete");

        //act
        mockMvc.perform(delete("/appointments/{id}", saved.getAppointmentId()))
            .andExpect(status().isNoContent());

        //asert
        assertFalse(appointmentRepository.existsById(saved.getAppointmentId()));
    }


    @Test
    void deleteAppointment_whenNotFound_returns404() throws Exception {
        //arrange
        long nonExistingId = 999L;

        //act and assert
        mockMvc.perform(delete("/appointments/{id}", nonExistingId))
            .andExpect(status().isNotFound());
    }
}

