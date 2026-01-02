// Controller -> talks to service -> returns DTOs to client
package nl.carsforyou.garage.controllers;

import jakarta.validation.Valid;
import nl.carsforyou.garage.dtos.ServiceOrder.ServiceOrderResponseDto;
import nl.carsforyou.garage.dtos.Vehicle.VehicleResponseDto;
import nl.carsforyou.garage.dtos.customer.CustomerRequestDto;
import nl.carsforyou.garage.dtos.customer.CustomerResponseDto;
import nl.carsforyou.garage.services.CustomerService;
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

@Tag(name = "Customers")
@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
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
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponseDto createCustomer(@Valid @RequestBody CustomerRequestDto dto) {
        return customerService.createCustomer(dto);
    }


    @Operation(summary = "Update a customer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer updated"),
            @ApiResponse(responseCode = "404", description = "Customer not found, check the {id}", content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    })
    @PutMapping("/{id}")
    public CustomerResponseDto updateCustomer(@Parameter(description = "Customer id", example = "1") @PathVariable Long id, @Valid @RequestBody CustomerRequestDto dto) {
        return customerService.updateCustomer(id, dto);
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

