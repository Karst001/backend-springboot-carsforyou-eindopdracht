// Controller -> talks to service -> returns DTOs to client
package nl.carsforyou.garage.controllers;

import jakarta.validation.Valid;
import nl.carsforyou.garage.dtos.appointment.AppointmentRequestDto;
import nl.carsforyou.garage.dtos.appointment.AppointmentResponseDto;
import nl.carsforyou.garage.services.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Appointments", description = "Endpoints for Appointments")
@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    //these annotations are needed for Swagger
    @Operation(summary = "Get all appointments")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "List of appointments returned")})
    @GetMapping
    public List<AppointmentResponseDto> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }


    @Operation(summary = "Get an appointment by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Appointment found"),
            @ApiResponse(responseCode = "404", description = "Appointment not found, check the {id}", content = @Content)
    })
    @GetMapping("/{id}")
    public AppointmentResponseDto getAppointmentById(@Parameter(description = "Appointment id", example = "1") @PathVariable Long id) {
        return appointmentService.getAppointmentById(id);
    }


    @Operation(summary = "Create an appointment")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Appointment created"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))
            )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentResponseDto createAppointment(@Valid @RequestBody AppointmentRequestDto dto) {
        return appointmentService.createAppointment(dto);
    }


    @Operation(summary = "Update an appointment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Appointment updated"),
            @ApiResponse(responseCode = "404", description = "Appointment not found, check the {id}", content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    })
    @PutMapping("/{id}")
    public AppointmentResponseDto updateAppointment(@Parameter(description = "Appointment id", example = "1") @PathVariable Long id, @Valid @RequestBody AppointmentRequestDto dto) {
        return appointmentService.updateAppointment(id, dto);
    }


    @Operation(summary = "Delete an appointment")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Appointment deleted"),
            @ApiResponse(responseCode = "404", description = "Appointment not found, check the {id}", content = @Content)}
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAppointment(@Parameter(description = "Appointment id", example = "1") @PathVariable Long id) {
        appointmentService.deleteAppointment(id);
    }
}
