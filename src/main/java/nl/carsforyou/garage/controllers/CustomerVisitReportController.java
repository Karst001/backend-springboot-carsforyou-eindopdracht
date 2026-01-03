package nl.carsforyou.garage.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import nl.carsforyou.garage.dtos.customer.CustomerVisitSummaryDto;
import nl.carsforyou.garage.services.CustomerVisitReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Customer Reports")
@RestController
@RequestMapping("/reports/customers")
public class CustomerVisitReportController {

    //the download functionality is implemented as a REST endpoint that returns a JSON overview showing how often a customer visited the garage and the total cost per completed maintenance visit.
    //the cost calculation is handled in the database, while aggregation and validation are done in the service layer to keep the code base clean because I do not want any SQL queries inside the API

    private final CustomerVisitReportService reportService;

    public CustomerVisitReportController(CustomerVisitReportService reportService) {
        this.reportService = reportService;
    }

    @Operation(
            summary = "Download visit overview with total costs per maintenance (JSON)",
            description = "Returns how often a customer visited the garage and the total cost per completed service order"
    )
    @ApiResponse(responseCode = "200", description = "Visit overview returned")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    @GetMapping("/{customerId}/visits")
    public CustomerVisitSummaryDto downloadCustomerVisits(@PathVariable Long customerId) {
        return reportService.getCustomerVisitSummary(customerId);
    }
}