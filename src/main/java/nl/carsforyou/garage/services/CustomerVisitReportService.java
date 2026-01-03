package nl.carsforyou.garage.services;

import nl.carsforyou.garage.dtos.customer.CustomerVisitSummaryDto;
import nl.carsforyou.garage.dtos.customer.RepairVisitCostDto;
import nl.carsforyou.garage.repositories.CustomerRepository;
import nl.carsforyou.garage.repositories.ServiceOrderRepository;
import nl.carsforyou.garage.repositories.RepairVisitCostProjection;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CustomerVisitReportService {

    private final ServiceOrderRepository serviceOrderRepository;
    private final CustomerRepository customerRepository;

    public CustomerVisitReportService(ServiceOrderRepository serviceOrderRepository,
                                      CustomerRepository customerRepository) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.customerRepository = customerRepository;
    }

    public CustomerVisitSummaryDto getCustomerVisitSummary(Long customerId) {
        //validate customer exists
        if (!customerRepository.existsById(customerId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Customer with id " + customerId + " not found");
        }

        //load report rows from database stored function
        List<RepairVisitCostProjection> rows = serviceOrderRepository.findVisitCostsByCustomer(customerId);

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
}
