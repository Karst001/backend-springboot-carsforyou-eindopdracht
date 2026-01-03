package nl.carsforyou.garage.dtos.customer;

import java.util.List;

public class CustomerVisitSummaryDto {
    private Long customerId;
    private int totalVisits;
    private List<RepairVisitCostDto> repairVisits;

    public CustomerVisitSummaryDto(Long customerId, int totalVisits, List<RepairVisitCostDto> repairVisits) {
        this.customerId = customerId;
        this.totalVisits = totalVisits;
        this.repairVisits = repairVisits;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public int getTotalVisits() {
        return totalVisits;
    }

    public void setTotalVisits(int totalVisits) {
        this.totalVisits = totalVisits;
    }

    public List<RepairVisitCostDto> getRepairVisits() {
        return repairVisits;
    }

    public void setRepairVisits(List<RepairVisitCostDto> repairVisits) {
        this.repairVisits = repairVisits;
    }
}
