package nl.carsforyou.garage.services;

import nl.carsforyou.garage.dtos.customer.CustomerServiceReportDto;
import nl.carsforyou.garage.dtos.customer.CustomerVisitSummaryDto;
import nl.carsforyou.garage.dtos.customer.RepairVisitCostDto;
import nl.carsforyou.garage.repositories.CustomerRepository;
import nl.carsforyou.garage.repositories.CustomerServiceReportProjection;
import nl.carsforyou.garage.repositories.ServiceOrderRepository;
import nl.carsforyou.garage.repositories.RepairVisitCostProjection;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;


@Service
public class CustomerVisitReportService {

    private final ServiceOrderRepository serviceOrderRepository;
    private final CustomerRepository customerRepository;

    public CustomerVisitReportService(ServiceOrderRepository serviceOrderRepository, CustomerRepository customerRepository) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.customerRepository = customerRepository;
    }

    public CustomerVisitSummaryDto getCustomerVisitSummary(Long customerId) {
        //validate customer exists
        if (!customerRepository.existsById(customerId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer with id " + customerId + " not found");
        }

        //load report rows from database stored function
        List<RepairVisitCostProjection> rows = serviceOrderRepository.findVisitCostsByCustomer_Procedure(customerId);

        //convert rows to  DTO as the database fields are like 'customer_id' and Java is like 'customerId'
        List<RepairVisitCostDto> visits = rows.stream()
                .map(row -> new RepairVisitCostDto(
                        row.getServiceOrderId(),
                        row.getVehicleId(),
                        row.getCompletedDate(),
                        row.getTotalCost()
                ))
                .toList();

        //create the result summary
        return new CustomerVisitSummaryDto(
                customerId,
                visits.size(),
                visits
        );

        //sample output:
        //{
        //    "customerId": 1,
        //       "totalVisits": 2,
        //       "repairVisits": [
        //    {
        //       "serviceOrderId": 10,
        //          "vehicleId": 3,
        //          "completedDate": "2025-12-05T00:00:00",
        //         "totalCost": 249.90
        //     },
        //     {
        //         "serviceOrderId": 12,
        //         "vehicleId": 3,
        //         "completedDate": "2025-12-19T00:00:00",
        //         "totalCost": 89.95
        //     }
        //    ]
        //}
    }


    public CustomerServiceReportDto getCustomerServiceReport(Long customerId) {

        if (!customerRepository.existsById(customerId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer with id " + customerId + " not found");
        }

        List<CustomerServiceReportProjection> rows = serviceOrderRepository.findCustomerServiceReport_Procedure(customerId);
        CustomerServiceReportProjection first = rows.getFirst();

        // Group by vehicle
        Map<Long, List<CustomerServiceReportProjection>> byVehicle = rows.stream()
                .collect(Collectors.groupingBy(
                        CustomerServiceReportProjection::getVehicleId,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        List<CustomerServiceReportDto.VehicleDto> vehicles = byVehicle.values().stream()
                .map(vRows -> {
                    CustomerServiceReportProjection vFirst = vRows.getFirst();

                    //Group by service order inside this vehicle
                    Map<Long, List<CustomerServiceReportProjection>> byOrder = vRows.stream()
                            .filter(r -> r.getServiceOrderId() != null)
                            .collect(Collectors.groupingBy(
                                    CustomerServiceReportProjection::getServiceOrderId,
                                    LinkedHashMap::new,
                                    Collectors.toList()
                            ));

                    List<CustomerServiceReportDto.ServiceOrderDto> serviceOrders = byOrder.values().stream()
                            .map(oRows -> {
                                CustomerServiceReportProjection oFirst = oRows.getFirst();

                                List<CustomerServiceReportDto.PartLineDto> parts = oRows.stream()
                                        .filter(row -> row.getServiceOrderPartId() != null)
                                        .map(row -> new CustomerServiceReportDto.PartLineDto(
                                                row.getServiceOrderPartId(),
                                                row.getPartId(),
                                                row.getItemNumber(),
                                                row.getItemDescription(),
                                                row.getQtyUsed(),
                                                row.getUnitCost(),
                                                row.getUnitPrice(),
                                                row.getLineTotal()
                                        ))
                                        .toList();

                                return new CustomerServiceReportDto.ServiceOrderDto(
                                        oFirst.getServiceOrderId(),
                                        oFirst.getServiceCompletedDate(),
                                        oFirst.getOrderTotal(),
                                        parts
                                );
                            })
                            .toList();

                    return new CustomerServiceReportDto.VehicleDto(
                            vFirst.getVehicleId(),
                            vFirst.getLicensePlate(),
                            vFirst.getVinNumber(),
                            vFirst.getMake(),
                            vFirst.getModel(),
                            serviceOrders
                    );
                })
                .toList();

        return new CustomerServiceReportDto(
                first.getCustomerId(),
                first.getFirstName(),
                first.getLastName(),
                first.getAddress(),
                first.getZipCode(),
                first.getCity(),
                first.getCountry(),
                first.getTelephoneNumber(),
                first.getEmailAddress(),
                vehicles
        );
    }


    public byte[] generateCustomerServiceReportPdf(Long customerId) {
        CustomerServiceReportDto report = getCustomerServiceReport(customerId);
        return renderCustomerServiceReportPdf(report);
    }


    //used AI to build this PDF report, was a first for me writing a PDF like this
    private byte[] renderCustomerServiceReportPdf(CustomerServiceReportDto report) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        try (PDDocument doc = new PDDocument();
             ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {

            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            float margin = 50;
            float y = page.getMediaBox().getHeight() - margin;

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {

                //Report title
                y = writeText(cs, margin, y, PDType1Font.HELVETICA_BOLD, 18, "Customer Service Report");
                y -= 8;

                //Customer header
                y = writeText(cs, margin, y, PDType1Font.HELVETICA_BOLD, 12, "Customer");
                y = writeText(cs, margin, y, PDType1Font.HELVETICA, 11, "ID: " + report.customerId());
                y = writeText(cs, margin, y, PDType1Font.HELVETICA, 11, "Name: " + nullSafe(report.firstName()) + " " + nullSafe(report.lastName()));
                y = writeText(cs, margin, y, PDType1Font.HELVETICA, 11,
                        "Address: " + nullSafe(report.address()) + ", " +
                                nullSafe(report.zipCode()) + " " + nullSafe(report.city()) + ", " + nullSafe(report.country()));
                y = writeText(cs, margin, y, PDType1Font.HELVETICA, 11, "Phone: " + nullSafe(report.telephoneNumber()));
                y = writeText(cs, margin, y, PDType1Font.HELVETICA, 11, "Email: " + nullSafe(report.emailAddress()));
                y -= 10;

                if (report.vehicles().isEmpty()) {
                    writeText(cs, margin, y, PDType1Font.HELVETICA_OBLIQUE, 11, "No completed service orders found for this customer.");
                } else {
                    for (var vehicle : report.vehicles()) {
                        y = writeText(cs, margin, y, PDType1Font.HELVETICA_BOLD, 12, "Vehicle: " + nullSafe(vehicle.make()) + " " + nullSafe(vehicle.model()));
                        y = writeText(cs, margin, y, PDType1Font.HELVETICA, 11,
                                "Vehicle ID: " + vehicle.vehicleId() +
                                        " | Plate: " + nullSafe(vehicle.licensePlate()) +
                                        " | VIN: " + nullSafe(vehicle.vinNumber()));

                        if (vehicle.serviceOrders() == null || vehicle.serviceOrders().isEmpty()) {
                            y = writeText(cs, margin + 15, y, PDType1Font.HELVETICA_OBLIQUE, 11, "No completed service orders.");
                            y -= 6;
                            continue;
                        }

                        for (var order : vehicle.serviceOrders()) {
                            String dateStr = order.serviceCompletedDate() == null ? "-" : order.serviceCompletedDate().format(dtf);

                            y = writeText(cs, margin + 10, y, PDType1Font.HELVETICA_BOLD, 11,
                                    "Service Order #" + order.serviceOrderId() + " | Completed: " + dateStr);

                            y = writeText(cs, margin + 20, y, PDType1Font.HELVETICA_BOLD, 10,
                                    "Item #     Description                          Qty   Unit Price   Line Total");

                            if (order.parts() == null || order.parts().isEmpty()) {
                                y = writeText(cs, margin + 20, y, PDType1Font.HELVETICA_OBLIQUE, 10, "(No parts recorded)");
                            } else {
                                for (var p : order.parts()) {
                                    String item = padRight(trimTo(p.itemNumber(), 10), 10);
                                    String desc = padRight(trimTo(p.itemDescription(), 35), 35);
                                    String qty  = padLeft(String.valueOf(p.qtyUsed()), 3);
                                    String unit = padLeft(money(p.unitPrice()), 10);
                                    String line = padLeft(money(p.lineTotal()), 10);

                                    String rowText = item + "  " + desc + "  " + qty + "  " + unit + "  " + line;
                                    y = writeText(cs, margin + 20, y, PDType1Font.HELVETICA, 10, rowText);
                                }
                            }

                            y = writeText(cs, margin + 20, y, PDType1Font.HELVETICA_BOLD, 10,
                                    "Order total: " + money(order.orderTotal()));
                            y -= 8;
                        }

                        y -= 6;
                    }
                }
            }

            doc.save(byteStream);
            return byteStream.toByteArray();

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not generate PDF report");
        }
    }


    //report helpers
    private float writeText(PDPageContentStream cs, float x, float y,
                            org.apache.pdfbox.pdmodel.font.PDFont font, int fontSize,
                            String text) throws IOException {
        cs.beginText();
        cs.setFont(font, fontSize);
        cs.newLineAtOffset(x, y);
        cs.showText(text == null ? "" : text);
        cs.endText();

        return y - 14;
    }


    private String money(BigDecimal v) {
        if (v == null) {
            return "0.00";
        }

        return v.setScale(2, java.math.RoundingMode.HALF_UP).toString();
    }

    private String nullSafe(String s) {
        return s == null ? "" : s;
    }

    private String trimTo(String s, int max) {
        if (s == null) {
            return "";
        }

        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }

    private String padRight(String s, int len) {
        if (s == null) {
            s = "";
        }

        if (s.length() >= len) {
            return s;
        }

        return s + " ".repeat(len - s.length());
    }

    private String padLeft(String s, int len) {
        if (s == null) {
            s = "";
        }

        if (s.length() >= len) {
            return s;
        }

        return " ".repeat(len - s.length()) + s;
    }

}

