// Used to return data back to client, contains read-only and calculated fields that are safe to return
package nl.carsforyou.garage.dtos.ServiceOrder;

import java.math.BigDecimal;

public class ServiceOrderPartResponseDto {
    private Long serviceOrderPartId;
    private Long serviceOrderId;            //Foreign key to ServiceOrder
    private Long partId;                    //Foreign key to Parts
    private BigDecimal unitCost;            //history unitCost
    private BigDecimal unitPrice;           //history unitPrice

    private int qtyUsed;

    public Long getServiceOrderPartId() {
        return serviceOrderPartId;
    }

    public void setServiceOrderPartId(Long serviceOrderPartId) {
        this.serviceOrderPartId = serviceOrderPartId;
    }

    public Long getServiceOrderId() {
        return serviceOrderId;
    }

    public void setServiceOrderId(Long serviceOrderId) {
        this.serviceOrderId = serviceOrderId;
    }

    public Long getPartId() {
        return partId;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQtyUsed() {
        return qtyUsed;
    }

    public void setQtyUsed(int qtyUsed) {
        this.qtyUsed = qtyUsed;
    }
}
