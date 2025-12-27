package nl.carsforyou.garage.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "Parts")
public class PartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partId;
    @Column(name = "item_number", nullable = false)
    private String itemNumber;
    @Column(name = "item_description", nullable = false)
    private String itemDescription;
    private int qtyOnHand;
    private int qtyOnOrder;
    @Column(name = "unit_cost", nullable = false)
    private BigDecimal unitCost;
    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    public PartEntity() {

    }

    public PartEntity(Long partId, String itemNumber, String itemDescription,
        int qtyOnHand, int qtyOnOrder, BigDecimal  unitCost, BigDecimal  unitPrice) {

        this.partId = partId;
        this.itemNumber = itemNumber;
        this.itemDescription = itemDescription;
        this.qtyOnHand = qtyOnHand;
        this.qtyOnOrder = qtyOnOrder;
        this.unitCost = unitCost;
        this.unitPrice = unitPrice;
    }

    public Long getPartId() {
        return partId;
    }

    public void setPartId(long partId) {
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

    public BigDecimal  getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal  unitCost) {
        this.unitCost = unitCost;
    }

    public BigDecimal  getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal  unitPrice) {
        this.unitPrice = unitPrice;
    }
}
