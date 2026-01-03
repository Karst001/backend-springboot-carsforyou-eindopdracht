// Controller -> talks to service -> returns DTOs to client
package nl.carsforyou.garage.controllers;

import jakarta.validation.Valid;
import nl.carsforyou.garage.dtos.customer.CustomerUploadRequestDto;
import nl.carsforyou.garage.dtos.customer.CustomerUploadResponseDto;
import nl.carsforyou.garage.helpers.UrlHelper;
import nl.carsforyou.garage.services.CustomerUploadService;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "CustomerUploads")
@RestController
@RequestMapping("/customeruploads")
public class CustomerUploadController {
    private final CustomerUploadService customerUploadService;
    private final UrlHelper urlHelper;

    public CustomerUploadController(CustomerUploadService customerUploadService, UrlHelper urlHelper) {
        this.customerUploadService = customerUploadService;
        this.urlHelper = urlHelper;
    }


    //these annotations are needed for Swagger
    @Operation(summary = "Get all uploads")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "List of customers-uploads returned")})
    @GetMapping
    public List<CustomerUploadResponseDto> getAllCustomerUploads() {
        return customerUploadService.getAllCustomerUploads();
    }


    @Operation(summary = "Get a customer-upload by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer-upload not found"),
            @ApiResponse(responseCode = "404", description = "Customer-upload not found, check the {id}", content = @Content)
    })
    @GetMapping("/{id}")
    public CustomerUploadResponseDto getCustomerById(@Parameter(description = "Customer-upload id", example = "1") @PathVariable Long id) {
        return customerUploadService.getCustomerUploadById(id);
    }


    @Operation(summary = "Upload a file for a customer")
    @ApiResponse(responseCode = "201", description = "Upload created")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<@NonNull CustomerUploadResponseDto> uploadCustomerFile(@RequestParam Long customerId, @RequestParam String description, @RequestParam("file") MultipartFile file) {
        CustomerUploadResponseDto created =
                customerUploadService.createCustomerUpload(customerId, description, file);

        //this will return 201 Created and a location header with the new Id
        return ResponseEntity
                .created(urlHelper.getCurrentUrlWithId(created.getUploadId()))
                .body(created);
    }


    @Operation(summary = "Update a customer-upload")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer-upload updated"),
            @ApiResponse(responseCode = "404", description = "Customer-upload not found, check the {id}", content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<@NonNull CustomerUploadResponseDto> updateCustomerUpload(@Parameter(description = "Customer-upload id", example = "1") @PathVariable Long id, @Valid @RequestBody CustomerUploadRequestDto dto) {
        CustomerUploadResponseDto updated =
                customerUploadService.updateCustomerUpload(id, dto);

        return ResponseEntity.ok(updated);
    }


    @Operation(summary = "Delete a customer-upload")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Customer-upload deleted"),
            @ApiResponse(responseCode = "404", description = "Customer-upload not found, check the {id}", content = @Content)}
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomerUpload(@PathVariable Long id) {
        customerUploadService.deleteCustomerUpload(id);
    }
}
