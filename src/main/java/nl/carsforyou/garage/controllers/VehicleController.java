// Controller -> talks to service -> returns DTOs to client
package nl.carsforyou.garage.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nl.carsforyou.garage.dtos.ServiceOrder.ServiceOrderResponseDto;
import nl.carsforyou.garage.dtos.Vehicle.VehicleRequestDto;
import nl.carsforyou.garage.dtos.Vehicle.VehicleResponseDto;
import nl.carsforyou.garage.services.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Vehicles")
@RestController
@RequestMapping("/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    //these annotations are needed for Swagger
    @Operation(summary = "Get all vehicles")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "List of vehicles returned")})
    @GetMapping
    public List<VehicleResponseDto> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }


    @Operation(summary = "Get a vehicle by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehicle not found"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found, check the {id}", content = @Content)
    })
    @GetMapping("/{id}")
    public VehicleResponseDto getVehicleById(@Parameter(description = "Customer id", example = "1") @PathVariable Long id) {
        return vehicleService.getVehicleById(id);
    }

    // GET /vehicles/{id}/serviceorders
    @Operation(summary = "Get list of ServiceOrders for a vehicle")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehicle not found"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found, check the {id}", content = @Content)
    })
    @GetMapping("/{id}/serviceorders")
    public List<ServiceOrderResponseDto> getServiceOrders(@PathVariable Long id) {
        return vehicleService.getServiceOrdersForVehicle(id);
    }


    @Operation(summary = "Create a vehicle")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Vehicle created"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))
            )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleResponseDto createVehicle(@Valid @RequestBody VehicleRequestDto dto) {
        return vehicleService.createVehicle(dto);
    }


    @Operation(summary = "Update a vehicle")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehicle updated"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found, check the {id}", content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    })
    @PutMapping("/{id}")
    public VehicleResponseDto updateVehicle(@Parameter(description = "Vehicle id", example = "1") @PathVariable Long id, @Valid @RequestBody VehicleRequestDto dto) {
        return vehicleService.updateVehicle(id, dto);
    }


    @Operation(summary = "Delete a customer")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Vehicle deleted"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found, check the {id}", content = @Content)}
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@Parameter(description = "Vehicle id", example = "1") @PathVariable Long id) {
        vehicleService.deleteVehicle(id);
    }
}
