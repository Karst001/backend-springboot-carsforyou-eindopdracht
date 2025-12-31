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
import nl.carsforyou.garage.dtos.ServiceOrder.ServiceOrderRequestDto;
import nl.carsforyou.garage.dtos.ServiceOrder.ServiceOrderResponseDto;
import nl.carsforyou.garage.services.ServiceOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ServiceOrders")
@RestController
@RequestMapping("/serviceorders")
public class ServiceOrderController {
    private final ServiceOrderService serviceOrderService;

    public ServiceOrderController(ServiceOrderService serviceOrderService) {
        this.serviceOrderService = serviceOrderService;
    }

    //these annotations are needed for Swagger
    @Operation(summary = "Get all service-orders")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "List of service-orders returned")})
    @GetMapping
    public List<ServiceOrderResponseDto> getAllServiceOrders() {
        return serviceOrderService.getAllServiceOrders();
    }


    @Operation(summary = "Get a service-orders by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Service-order not found"),
            @ApiResponse(responseCode = "404", description = "Service-order not found, check the {id}", content = @Content)
    })
    @GetMapping("/{id}")
    public ServiceOrderResponseDto getServiceOrderById(@Parameter(description = "Customer id", example = "1") @PathVariable Long id) {
        return serviceOrderService.getServiceOrderById(id);
    }


    @Operation(summary = "Create a service-order")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Service-order created"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))
            )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceOrderResponseDto createServiceOrder(@Valid @RequestBody ServiceOrderRequestDto dto) {
        return serviceOrderService.createServiceOrder(dto);
    }


    @Operation(summary = "Update a service-order")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Service-order updated"),
            @ApiResponse(responseCode = "404", description = "Service-order not found, check the {id}", content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    })
    @PutMapping("/{id}")
    public ServiceOrderResponseDto updateServiceOrder(@Parameter(description = "Customer id", example = "1") @PathVariable Long id, @Valid @RequestBody ServiceOrderRequestDto dto) {
        return serviceOrderService.updateServiceOrder(id, dto);
    }


    @Operation(summary = "Delete a service-order")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Service-order deleted"),
            @ApiResponse(responseCode = "404", description = "Service-order not found, check the {id}", content = @Content)}
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteServiceOrder(@Parameter(description = "Service-order id", example = "1") @PathVariable Long id) {
        serviceOrderService.deleteServiceOrder(id);
    }
}
