package nl.carsforyou.garage.repositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface RepairVisitCostProjection {
    Long getServiceOrderId();
    Long getVehicleId();
    LocalDateTime getCompletedDate();
    BigDecimal getTotalCost();
}