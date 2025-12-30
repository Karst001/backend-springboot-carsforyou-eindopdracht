// Controller -> talks to service -> returns DTOs to client
package nl.carsforyou.garage.controllers;

import jakarta.validation.Valid;
import nl.carsforyou.garage.dtos.customer.CustomerUploadRequestDto;
import nl.carsforyou.garage.dtos.customer.CustomerUploadResponseDto;
import nl.carsforyou.garage.services.CustomerUploadService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "CustomerUploads", description = "Endpoints for Customer file uploads")
@RestController
@RequestMapping("/customeruploads")
public class CustomerUploadController {
    private final CustomerUploadService customerUploadService;

    public CustomerUploadController(CustomerUploadService customerUploadService) {
        this.customerUploadService = customerUploadService;
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
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerUploadResponseDto uploadCustomerFile(@RequestParam Long customerId, @RequestParam("file") MultipartFile file) {
        return customerUploadService.createCustomerUpload(customerId, file);
    }


    @Operation(summary = "Update a customer-upload")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer-upload updated"),
            @ApiResponse(responseCode = "404", description = "Customer-upload not found, check the {id}", content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    })
    @PutMapping("/{id}")
    public CustomerUploadResponseDto updateCustomerUpload(@Parameter(description = "Customer-upload id", example = "1") @PathVariable Long id, @Valid @RequestBody CustomerUploadRequestDto dto) {
        return customerUploadService.updateCustomerUpload(id, dto);
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
