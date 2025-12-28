// Used to return data back to client, contains read-only and calculated fields that are safe to return
package nl.carsforyou.garage.dtos.ServiceOrder;

import java.time.LocalDateTime;

public class ServiceOrderResponseDto {
    private Long serviceOrderId;
    private LocalDateTime serviceCompletedDate;
    private Long vehicleId;

    public Long getServiceOrderId() {
        return serviceOrderId;
    }

    public void setServiceOrderId(Long serviceOrderId) {
        this.serviceOrderId = serviceOrderId;
    }

    public LocalDateTime getServiceCompletedDate() {
        return serviceCompletedDate;
    }

    public void setServiceCompletedDate(LocalDateTime serviceCompletedDate) {
        this.serviceCompletedDate = serviceCompletedDate;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }
}
