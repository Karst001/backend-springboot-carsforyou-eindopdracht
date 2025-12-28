// Used to return data back to client, contains read-only and calculated fields that are safe to return
package nl.carsforyou.garage.dtos.Vehicle;

import java.math.BigDecimal;

public class VehicleResponseDto {
    private Long vehicleId;
    private String licensePlate;
    private String vinNumber;
    private String make;
    private String model;
    private String paintColor;
    private String bodyStyle;
    private Boolean trailerHitch;
    private BigDecimal costPrice;                   //for this assignment I will show it but real world application
                                                    //this may only be visible for Admins and not for a Customer
    private BigDecimal salePrice;
    private Long customerId;

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

    public void setTrailerHitch(Boolean trailerHitch) {
        this.trailerHitch = trailerHitch;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
