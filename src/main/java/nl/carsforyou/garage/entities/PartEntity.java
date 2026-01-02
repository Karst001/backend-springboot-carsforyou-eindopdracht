package nl.carsforyou.garage.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parts")
public class PartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "part_id")
    private Long partId;

    @Column(name = "item_number", nullable = false)
    private String itemNumber;

    @Column(name = "item_description", nullable = false)
    private String itemDescription;

    @Column(name = "qty_on_hand")
    private int qtyOnHand;

    @Column(name = "qty_on_order")
    private int qtyOnOrder;

    @Column(name = "unit_cost", nullable = false)
    private BigDecimal unitCost;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    //As per database diagram, 1 Part can appear in many ServiceOrderPart records, one-to-many relation
    @JsonIgnore
    @OneToMany(mappedBy = "part", fetch = FetchType.LAZY)
    private List<ServiceOrderPartEntity> serviceOrderParts = new ArrayList<>();

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

    //base getters/setters
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


    //added getters/setters for the relation
    public List<ServiceOrderPartEntity> getServiceOrderParts() {
        return serviceOrderParts;
    }

    public void setServiceOrderParts(List<ServiceOrderPartEntity> serviceOrderParts) {
        this.serviceOrderParts = serviceOrderParts;
    }
}
