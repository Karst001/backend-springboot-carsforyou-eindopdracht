// Used to receive data from client during CRUD, contains fields that the client is allowed to send
package nl.carsforyou.garage.dtos.Vehicle;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class VehicleRequestDto {
    @NotBlank
    private String licensePlate;

    @NotBlank
    private String vinNumber;

    @NotBlank
    private String make;

    @NotBlank
    private String model;

    //optional
    private String paintColor;
    private String bodyStyle;
    private Boolean trailerHitch;

    @PositiveOrZero                 //must be 0 or greater
    private BigDecimal costPrice;

    @PositiveOrZero
    private BigDecimal salePrice;

    @NotNull
    private Long customerId;


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
