package nl.carsforyou.garage.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "vehicles")
public class VehicleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vehicleId;
    @Column(name = "license_plate", nullable = false)
    private String licensePlate;
    @Column(name = "vin_number", nullable = false)
    private String vinNumber;
    @Column(name = "make", nullable = false)
    private String make;
    @Column(name = "model", nullable = false)
    private String model;
    private String paintColor;
    private String bodyStyle;
    private Boolean trailerHitch;
    private BigDecimal  costPrice;
    private BigDecimal  salePrice;
    private Long customerId;

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

    public Boolean isTrailerHitch() {
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
}
