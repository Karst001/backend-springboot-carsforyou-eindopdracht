package nl.carsforyou.garage.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "service_order_parts")
public class ServiceOrderPartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_order_part_id")
    private Long serviceOrderPartId;

    @Column(name = "service_order_id", nullable = false, insertable = false, updatable = false)
    private Long serviceOrderId;

    //'insertable/updatable' used as it maps the same column in the database
    @Column(name = "part_id", nullable = false, insertable = false, updatable = false)
    private Long partId;

    @Column(name = "unit_cost", nullable = false)
    private BigDecimal unitCost;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal  unitPrice;

    @Column(name = "qty_used", nullable = false)
    private int qtyUsed;

    //as per database diagram, this is a many-to-one relation and this the owner of that relation
    //many service order part records can reference the same Part, like a generic oil filter can be used on many different cars
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "part_id", referencedColumnName = "part_id")
    private PartEntity part;

    //as per the database diagran, this is the owning side of the ServiceOrder relation
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_order_id", referencedColumnName = "service_order_id")
    private ServiceOrderEntity serviceOrder;

    public ServiceOrderPartEntity() {}

    public ServiceOrderPartEntity(Long serviceOrderPartId, Long serviceOrderId, Long partId, BigDecimal unitCost,
        BigDecimal unitPrice, int qtyUsed) {

        this.serviceOrderPartId = serviceOrderPartId;
        this.serviceOrderId = serviceOrderId;
        this.partId = partId;
        this.unitCost = unitCost;
        this.unitPrice = unitPrice;
        this.qtyUsed = qtyUsed;
    }

    //standard getter/setters
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

    public int getQtyUsed() {
        return qtyUsed;
    }

    public void setQtyUsed(int qtyUsed) {
        this.qtyUsed = qtyUsed;
    }


    //added getter/setter for the realtion
    public PartEntity getPart() {
        return part;
    }

    public void setPart(PartEntity part) {
        this.part = part;
    }

    public ServiceOrderEntity getServiceOrder() {
        return serviceOrder;
    }

    public void setServiceOrder(ServiceOrderEntity serviceOrder) {
        this.serviceOrder = serviceOrder;
    }
}
