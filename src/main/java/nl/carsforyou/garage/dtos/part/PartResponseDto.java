// Used to return data back to client, contains read-only and calculated fields that are safe to return
package nl.carsforyou.garage.dtos.part;

import java.math.BigDecimal;

public class PartResponseDto {
    private Long partId;
    private String itemNumber;
    private String itemDescription;
    private int qtyOnHand;
    private int qtyOnOrder;
    private BigDecimal unitCost;        //for this assignment I will show it but real world application
                                        //this may only be visible for Admins and not for a Customer
    private BigDecimal unitPrice;

    public Long getPartId() {
        return partId;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public int getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(int qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }

    public int getQtyOnOrder() {
        return qtyOnOrder;
    }

    public void setQtyOnOrder(int qtyOnOrder) {
        this.qtyOnOrder = qtyOnOrder;
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
}
