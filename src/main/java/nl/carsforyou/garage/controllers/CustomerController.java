// Controller -> talks to service -> returns DTOs to client
package nl.carsforyou.garage.controllers;

import jakarta.validation.Valid;
import nl.carsforyou.garage.dtos.Vehicle.VehicleResponseDto;
import nl.carsforyou.garage.dtos.customer.CustomerRequestDto;
import nl.carsforyou.garage.dtos.customer.CustomerResponseDto;
import nl.carsforyou.garage.helpers.UrlHelper;
import nl.carsforyou.garage.services.CustomerService;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Customers")
@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;
    private final UrlHelper urlHelper;

    public CustomerController(CustomerService customerService, UrlHelper urlHelper) {
        this.customerService = customerService;
        this.urlHelper = urlHelper;
    }

    //these annotations are needed for Swagger
    @Operation(summary = "Get all customers")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "List of customers returned")})
    @GetMapping
    public List<CustomerResponseDto> getAllCustomers() {
        return customerService.getAllCustomers();
    }


    @Operation(summary = "Get a customer by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer not found"),
            @ApiResponse(responseCode = "404", description = "Customer not found, check the {id}", content = @Content)
    })
    @GetMapping("/{id}")
    public CustomerResponseDto getCustomerById(@Parameter(description = "Customer id", example = "1") @PathVariable Long id) {
        return customerService.getCustomerById(id);
    }


    // GET /customer/{id}/vehicles
    @Operation(summary = "Get list of Vehicles for a customer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer not found"),
            @ApiResponse(responseCode = "404", description = "Customer not found, check the {id}", content = @Content)
    })
    @GetMapping("/{id}/vehicles")
    public List<VehicleResponseDto> getVehicles(@PathVariable Long id) {
        return customerService.getVehiclesForCustomer(id);
    }


    @Operation(summary = "Create a customer")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Customer created"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))
            )
    })
    @PostMapping
    public ResponseEntity<@NonNull CustomerResponseDto> createCustomer(@Valid @RequestBody CustomerRequestDto dto) {
        CustomerResponseDto created = customerService.createCustomer(dto);

        //this will return 201 Created and a location header with the new Id
        return ResponseEntity
                .created(urlHelper.getCurrentUrlWithId(created.getCustomerId()))
                .body(created);
    }


    @Operation(summary = "Update a customer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer updated"),
            @ApiResponse(responseCode = "404", description = "Customer not found, check the {id}", content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<@NonNull CustomerResponseDto> updateCustomer(@Parameter(description = "Customer id", example = "1") @PathVariable Long id, @Valid @RequestBody CustomerRequestDto dto) {
        CustomerResponseDto updated = customerService.updateCustomer(id, dto);

        return ResponseEntity.ok(updated);
    }


    @Operation(summary = "Delete a customer")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Customer deleted"),
            @ApiResponse(responseCode = "404", description = "Customer not found, check the {id}", content = @Content)}
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@Parameter(description = "Customer id", example = "1") @PathVariable Long id) {
        customerService.deleteCustomer(id);
    }
}

