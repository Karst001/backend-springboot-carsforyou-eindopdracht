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
import nl.carsforyou.garage.dtos.customer.CustomerRequestDto;
import nl.carsforyou.garage.dtos.customer.CustomerResponseDto;
import nl.carsforyou.garage.dtos.part.PartRequestDto;
import nl.carsforyou.garage.dtos.part.PartResponseDto;
import nl.carsforyou.garage.services.CustomerUploadService;
import nl.carsforyou.garage.services.PartService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Parts", description = "Endpoints for Parts")
@RestController
@RequestMapping("/parts")
public class PartController {
    private final PartService partService;

    public PartController(PartService partService) {
        this.partService = partService;
    }

    //these annotations are needed for Swagger
    @Operation(summary = "Get all parts")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "List of parts returned")})
    @GetMapping
    public List<PartResponseDto> getAllParts() {
        return partService.getAllParts();
    }


    @Operation(summary = "Get a part by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "part not found"),
            @ApiResponse(responseCode = "404", description = "Part not found, check the {id}", content = @Content)
    })
    @GetMapping("/{id}")
    public PartResponseDto getPartById(@Parameter(description = "Part id", example = "1") @PathVariable Long id) {
        return partService.getPartById(id);
    }


    @Operation(summary = "Create a part")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Part created"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))
            )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PartResponseDto createPart(@Valid @RequestBody PartRequestDto dto) {
        return partService.createPart(dto);
    }


    @Operation(summary = "Update a part")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Part updated"),
            @ApiResponse(responseCode = "404", description = "Part not found, check the {id}", content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    })
    @PutMapping("/{id}")
    public PartResponseDto updatePart(@Parameter(description = "Customer id", example = "1") @PathVariable Long id, @Valid @RequestBody PartRequestDto dto) {
        return partService.updatePart(id, dto);
    }


    @Operation(summary = "Delete a part")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Part deleted"),
            @ApiResponse(responseCode = "404", description = "Part not found, check the {id}", content = @Content)}
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePart(@Parameter(description = "Part id", example = "1") @PathVariable Long id) {
        partService.deletePart(id);
    }
}
