package nl.carsforyou.garage.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicles")
public class VehicleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id")
    private Long vehicleId;

    @Column(name = "license_plate", nullable = false)
    private String licensePlate;

    @Column(name = "vin_number", nullable = false)
    private String vinNumber;

    @Column(name = "make", nullable = false)
    private String make;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "paint_color")
    private String paintColor;

    @Column(name = "body_style")
    private String bodyStyle;

    @Column(name = "trailer_hitch")
    private Boolean trailerHitch;

    @Column(name = "cost_price")
    private BigDecimal  costPrice;

    @Column(name = "sale_price")
    private BigDecimal  salePrice;

    @Column(name = "customer_id", nullable = false, insertable = false, updatable = false)
    private Long customerId;

    //as per database diagram, many-to-one relation, owner side of the relation
    //many vehicle can belong to one customer
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private CustomerEntity customer;

    //as per the database diagram, one vehicle can have many service orders
    @JsonIgnore
    @OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY)
    private List<ServiceOrderEntity> serviceOrders = new ArrayList<>();


    //as per databse diagram, one vehicle can have many appointments
    @JsonIgnore
    @OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY)
    private List<AppointmentEntity> appointments = new ArrayList<>();

    public VehicleEntity() {}

    public VehicleEntity(Long vehicleId, String licensePlate, String vinNumber,
        String make, String model, String paintColor, String bodyStyle,
        boolean trailerHitch, BigDecimal  costPrice, BigDecimal  salePrice, Long customerId) {

        this.vehicleId = vehicleId;
        this.licensePlate = licensePlate;
        this.vinNumber = vinNumber;
        this.make = make;
        this.model = model;
        this.paintColor = paintColor;
        this.bodyStyle = bodyStyle;
        this.trailerHitch = trailerHitch;
        this.costPrice = costPrice;
        this.salePrice = salePrice;
        this.customerId = customerId;
    }

    //basic getters/setters
    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getVinNumber() {
        return vinNumber;
    }

    public void setVinNumber(String vinNumber) {
        this.vinNumber = vinNumber;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPaintColor() {
        return paintColor;
    }

    public void setPaintColor(String paintColor) {
        this.paintColor = paintColor;
    }

    public String getBodyStyle() {
        return bodyStyle;
    }

    public void setBodyStyle(String bodyStyle) {
        this.bodyStyle = bodyStyle;
    }

    public Boolean getTrailerHitch() {
        return trailerHitch;
    }

    public void setTrailerHitch(boolean trailerHitch) {
        this.trailerHitch = trailerHitch;
    }

    public BigDecimal  getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal  costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal  getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    //getters/setters for relation to Appointments
    public List<AppointmentEntity> getAppointments() {
        return appointments;
    }
    public void setAppointments(List<AppointmentEntity> appointments) {
        this.appointments = appointments;
    }

    //added for the Customer relation
    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }


    //getter/setter for the ServiceOrder relation
    public List<ServiceOrderEntity> getServiceOrders() {
        return serviceOrders;
    }

    public void setServiceOrders(List<ServiceOrderEntity> serviceOrders) {
        this.serviceOrders = serviceOrders;
    }
}
