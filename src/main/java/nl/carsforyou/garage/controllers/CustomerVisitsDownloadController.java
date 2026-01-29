//package nl.carsforyou.garage.controllers;
//
//import io.swagger.v3.oas.annotations.tags.Tag;
//import nl.carsforyou.garage.dtos.customer.CustomerVisitSummaryDto;
//import nl.carsforyou.garage.services.CustomerVisitReportService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import org.springframework.web.bind.annotation.*;
//
//@Tag(name = "Customer Reports")
//@RestController
//@RequestMapping("/reports/customers")
//public class CustomerVisitReportController {
//
//    //the download functionality is implemented as a REST endpoint that returns a JSON overview showing how often a customer visited the garage and the total cost per completed maintenance visit.
//    //the cost calculation is handled in the database, while aggregation and validation are done in the service layer to keep the code base clean because I do not want any SQL queries inside the API
//
//    private final CustomerVisitReportService reportService;
//
//    public CustomerVisitReportController(CustomerVisitReportService reportService) {
//        this.reportService = reportService;
//    }
//
//    @Operation(
//            summary = "Download visit overview with total costs per maintenance (JSON)",
//            description = "Returns how often a customer visited the garage and the total cost per completed service order"
//    )
//    @ApiResponse(responseCode = "200", description = "Visit overview returned")
//    @ApiResponse(responseCode = "404", description = "Customer not found")
//    @GetMapping("/{customerId}/visits")
//    public CustomerVisitSummaryDto downloadCustomerVisits(@PathVariable Long customerId) {
//        return reportService.getCustomerVisitSummary(customerId);
//    }
//}

package nl.carsforyou.garage.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import nl.carsforyou.garage.dtos.customer.CustomerServiceReportDto;
import nl.carsforyou.garage.dtos.customer.CustomerVisitSummaryDto;
import nl.carsforyou.garage.services.CustomerVisitReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Customer downloadable Reports")
@RestController
@RequestMapping("/downloads/customers")
public class CustomerVisitsDownloadController {

    //the download functionality is implemented as a REST endpoint that returns a JSON overview showing how often a customer visited the garage and the total cost per completed maintenance visit.
    //the cost calculation is handled in the database, while aggregation and validation are done in the service layer to keep the code base clean because I do not want any SQL queries inside the API

    private final CustomerVisitReportService reportService;

    public CustomerVisitsDownloadController(CustomerVisitReportService reportService) {
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


    @Operation(
            summary = "Get full customer service report (JSON)",
            description = "Returns customer details, vehicles, service orders, and parts used"
    )
    @ApiResponse(responseCode = "200", description = "Service report returned")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    @GetMapping("/{customerId}/service-report")
    public CustomerServiceReportDto getCustomerServiceReport(@PathVariable Long customerId) {
        return reportService.getCustomerServiceReport(customerId);
    }


    @Operation(
            summary = "Download full customer service report (PDF)",
            description = "Returns a PDF report with customer details, vehicles, service orders, and parts used"
    )
    @ApiResponse(responseCode = "200", description = "PDF report returned")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    @GetMapping(value = "/{customerId}/service-report.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> downloadCustomerServiceReportPdf(@PathVariable Long customerId) {
        byte[] pdfBytes = reportService.generateCustomerServiceReportPdf(customerId);
        String filename = "customer-" + customerId + "-service-report.pdf";
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(pdfBytes);
    }
}