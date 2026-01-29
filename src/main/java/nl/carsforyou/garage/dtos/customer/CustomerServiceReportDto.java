package nl.carsforyou.garage.dtos.customer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CustomerServiceReportDto(Long customerId, String firstName, String lastName, String address,
       String zipCode, String city, String country, String telephoneNumber, String emailAddress, List<VehicleDto> vehicles)
{

    public record VehicleDto(Long vehicleId, String licensePlate, String vinNumber, String make, String model, List<ServiceOrderDto> serviceOrders) {}

    public record ServiceOrderDto(Long serviceOrderId, LocalDateTime serviceCompletedDate, BigDecimal orderTotal, List<PartLineDto> parts) {}

    public record PartLineDto(Long serviceOrderPartId, Long partId, String itemNumber, String itemDescription,
            Integer qtyUsed, BigDecimal unitCost, BigDecimal unitPrice, BigDecimal lineTotal) {}

}

