// Repository -> returns entities
package nl.carsforyou.garage.repositories;

import nl.carsforyou.garage.entities.ServiceOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrderEntity, Long> {
    //check if there is a relation to Vehicle
    boolean existsByVehicle_VehicleId(Long customerId);

    //returns all service orders that belong to selected vehicle
    List<ServiceOrderEntity> findAllByVehicle_VehicleId(Long vehicleId);

    //below code maps the database fields to the Java entity fields
    @Query(value = """
        SELECT
            service_order_id          AS serviceOrderId,
            vehicle_id                AS vehicleId,
            service_completed_date    AS completedDate,
            total_cost                AS totalCost
        FROM get_customer_visit_costs(:customerId)
        """, nativeQuery = true)
    List<RepairVisitCostProjection> findVisitCostsByCustomer(@Param("customerId") Long customerId);
}
