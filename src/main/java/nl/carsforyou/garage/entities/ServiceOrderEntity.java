package nl.carsforyou.garage.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "ServiceOrders")
public class ServiceOrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceOrderId;
    private Date serviceCompletedDate;
    @Column(name = "vehicle_id", nullable = false)
    private Long vehicleId;

    public ServiceOrderEntity() {}

    public ServiceOrderEntity(Long serviceOrderId, Date serviceCompletedDate, Long vehicleId) {
        this.serviceOrderId = serviceOrderId;
        this.serviceCompletedDate = serviceCompletedDate;
        this.vehicleId = vehicleId;
    }

    public Long getServiceOrderId() {
        return serviceOrderId;
    }

    public void setServiceOrderId(Long serviceOrderId) {
        this.serviceOrderId = serviceOrderId;
    }

    public Date getServiceCompletedDate() {
        return serviceCompletedDate;
    }

    public void setServiceCompletedDate(Date serviceCompletedDate) {
        this.serviceCompletedDate = serviceCompletedDate;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }
}
