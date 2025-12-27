package nl.carsforyou.garage.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "service_order_parts")
public class ServiceOrderPartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceOrderPartId;
    @Column(name = "service_id", nullable = false)
    private Long serviceId;
    @Column(name = "part_id", nullable = false)
    private Long partId;
    @Column(name = "unit_cost", nullable = false)
    private BigDecimal unitCost;
    @Column(name = "unit_price", nullable = false)
    private BigDecimal  unitPrice;
    @Column(name = "qty_used", nullable = false)
    private int qtyUsed;

    public ServiceOrderPartEntity() {}

    public ServiceOrderPartEntity(Long serviceOrderPartId, Long serviceId, Long partId, BigDecimal unitCost,
        BigDecimal unitPrice, int qtyUsed) {

        this.serviceOrderPartId = serviceOrderPartId;
        this.serviceId = serviceId;
        this.partId = partId;
        this.unitCost = unitCost;
        this.unitPrice = unitPrice;
        this.qtyUsed = qtyUsed;
    }

    public Long getServiceOrderPartId() {
        return serviceOrderPartId;
    }

    public void setServiceOrderPartId(Long serviceOrderPartId) {
        this.serviceOrderPartId = serviceOrderPartId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
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
}
