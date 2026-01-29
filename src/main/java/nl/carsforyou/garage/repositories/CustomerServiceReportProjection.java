package nl.carsforyou.garage.repositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface CustomerServiceReportProjection {
    //Customer details for the header
    Long getCustomerId();
    String getFirstName();
    String getLastName();
    String getAddress();
    String getZipCode();
    String getCity();
    String getCountry();
    String getTelephoneNumber();
    String getEmailAddress();

    //Vehicle details
    Long getVehicleId();
    String getLicensePlate();
    String getVinNumber();
    String getMake();
    String getModel();

    //Service order details
    Long getServiceOrderId();
    LocalDateTime getServiceCompletedDate();

    //Parts details
    Long getServiceOrderPartId();
    Long getPartId();
    String getItemNumber();
    String getItemDescription();
    Integer getQtyUsed();
    BigDecimal getUnitCost();
    BigDecimal getUnitPrice();
    BigDecimal getLineTotal();

    //totals
    BigDecimal getOrderTotal();
}
