package nl.carsforyou.garage.dtos.customer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RepairVisitCostDto {
    private Long serviceOrderId;
    private Long vehicleId;
    private LocalDateTime completedDate;
    private BigDecimal totalCost;

    public RepairVisitCostDto(Long serviceOrderId, Long vehicleId, LocalDateTime completedDate, BigDecimal totalCost) {
        this.serviceOrderId = serviceOrderId;
        this.vehicleId = vehicleId;
        this.completedDate = completedDate;
        this.totalCost = totalCost;
    }

    public Long getServiceOrderId() {
        return serviceOrderId;
    }

    public void setServiceOrderId(Long serviceOrderId) {
        this.serviceOrderId = serviceOrderId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public LocalDateTime getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(LocalDateTime completedDate) {
        this.completedDate = completedDate;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
}
