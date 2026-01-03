package nl.carsforyou.garage.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nl.carsforyou.garage.dtos.ServiceOrder.ServiceOrderPartRequestDto;
import nl.carsforyou.garage.dtos.ServiceOrder.ServiceOrderPartResponseDto;
import nl.carsforyou.garage.helpers.UrlHelper;
import nl.carsforyou.garage.services.ServiceOrderPartService;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ServiceOrderParts")
@RestController
@RequestMapping("/serviceorderparts")
public class ServiceOrderPartController {
    private final ServiceOrderPartService serviceOrderPartService;
    private final UrlHelper urlHelper;

    public ServiceOrderPartController(ServiceOrderPartService serviceOrderPartService, UrlHelper urlHelper) {
        this.serviceOrderPartService = serviceOrderPartService;
        this.urlHelper = urlHelper;
    }

    //these annotations are needed for Swagger
    @Operation(summary = "Get all parts")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "List of server-order parts returned")})
    @GetMapping
    public List<ServiceOrderPartResponseDto> getAllServiceOrderParts() {
        return serviceOrderPartService.getAllServiceOrderParts();
    }


    @Operation(summary = "Get a service-order part by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Service-order part not found"),
            @ApiResponse(responseCode = "404", description = "Service-order part not found, check the {id}", content = @Content)
    })
    @GetMapping("/{id}")
    public ServiceOrderPartResponseDto getServiceOrderPartById(@Parameter(description = "Service-order part id", example = "1") @PathVariable Long id) {
        return serviceOrderPartService.getServiceOrderPartById(id);
    }


    @Operation(summary = "Create a service-order part")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Service-order part created"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))
            )
    })
    @PostMapping
    public ResponseEntity<@NonNull ServiceOrderPartResponseDto> createServiceOrderPart(@Valid @RequestBody ServiceOrderPartRequestDto dto) {
        ServiceOrderPartResponseDto created =
                serviceOrderPartService.createServiceOrderPart(dto);

        //this will return 201 Created and a location header with the new Id
        return ResponseEntity
                .created(urlHelper.getCurrentUrlWithId(created.getServiceOrderPartId()))
                .body(created);
    }


    @Operation(summary = "Update a service-order part")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Service-order part updated"),
            @ApiResponse(responseCode = "404", description = "Service-order part not found, check the {id}", content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<@NonNull ServiceOrderPartResponseDto> updateServiceOrderPart(@Parameter(description = "Customer id", example = "1") @PathVariable Long id, @Valid @RequestBody ServiceOrderPartRequestDto dto) {
        ServiceOrderPartResponseDto updated = serviceOrderPartService.updateServiceOrderPart(id, dto);

        return ResponseEntity.ok(updated);
    }


    @Operation(summary = "Delete a service-order part")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Service-order part deleted"),
            @ApiResponse(responseCode = "404", description = "Service-order part not found, check the {id}", content = @Content)}
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteServiceOrderPart(@Parameter(description = "Service-order part id", example = "1") @PathVariable Long id) {
        serviceOrderPartService.deleteServiceOrderPart(id);
    }
}
