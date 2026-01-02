package nl.carsforyou.garage.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "service_orders")
public class ServiceOrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_order_id")
    private Long serviceOrderId;

    @Column(name = "service_completed_date")
    private LocalDateTime serviceCompletedDate;

    @Column(name = "vehicle_id", nullable = false, insertable = false, updatable = false)
    private Long vehicleId;


    //as per database diagram, one ServiceOrder can have many ServiceOrderPart records
    @JsonIgnore
    @OneToMany(mappedBy = "serviceOrder", fetch = FetchType.LAZY)
    private List<ServiceOrderPartEntity> serviceOrderParts = new ArrayList<>();

    // as per database diagram, many-to-one relation, and is the owner side of this relation
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicle_id", referencedColumnName = "vehicle_id")
    private VehicleEntity vehicle;

    public ServiceOrderEntity() {}

    //basic getters/setters
    public ServiceOrderEntity(Long serviceOrderId, LocalDateTime serviceCompletedDate, Long vehicleId) {
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


    //added getters/setters
    public List<ServiceOrderPartEntity> getServiceOrderParts() {
        return serviceOrderParts;
    }
    public void setServiceOrderParts(List<ServiceOrderPartEntity> serviceOrderParts) {
        this.serviceOrderParts = serviceOrderParts;
    }

    //getter/setter for the relation to Vehicles
    public VehicleEntity getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleEntity vehicle) {
        this.vehicle = vehicle;
    }
}
