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
import nl.carsforyou.garage.helpers.UrlHelper;
import nl.carsforyou.garage.services.VehicleService;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Vehicles")
@RestController
@RequestMapping("/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;
    private final UrlHelper urlHelper;

    public VehicleController(VehicleService vehicleService, UrlHelper urlHelper) {
        this.vehicleService = vehicleService;
        this.urlHelper = urlHelper;
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
    public ResponseEntity<@NonNull VehicleResponseDto> createVehicle(@Valid @RequestBody VehicleRequestDto dto) {
        VehicleResponseDto created = vehicleService.createVehicle(dto);

        //this will return 201 Created and a location header with the new Id
        return ResponseEntity
                .created(urlHelper.getCurrentUrlWithId(created.getVehicleId()))
                .body(created);
    }


    @Operation(summary = "Update a vehicle")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehicle updated"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found, check the {id}", content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<@NonNull VehicleResponseDto> updateVehicle(@Parameter(description = "Vehicle id", example = "1") @PathVariable Long id, @Valid @RequestBody VehicleRequestDto dto) {
        VehicleResponseDto updated = vehicleService.updateVehicle(id, dto);

        return ResponseEntity.ok(updated);
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
