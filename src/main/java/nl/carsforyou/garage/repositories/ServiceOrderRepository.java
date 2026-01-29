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

    //stored function that returns all service orders for a selected vehicle
    //converts the database fields into the Java field names
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
    List<RepairVisitCostProjection> findVisitCostsByCustomer_Procedure(@Param("customerId") Long customerId);



    //stored function that returns all details for a customer, then parsed to a PDF
    //converts the database fields into the Java field names
    @Query(value = """
        SELECT
            customer_id             AS customerId,               --customer detals
            first_name              AS firstName,
            last_name               AS lastName,
            address                 AS address,
            zip_code                AS zipCode,
            city                    AS city,
            country                 AS country,
            telephone_number        AS telephoneNumber,
            email_address           AS emailAddress,
            vehicle_id              AS vehicleId,               --vehicle details
            license_plate           AS licensePlate,
            vin_number              AS vinNumber,
            make                    AS make,
            model                   AS model,
            service_order_id        AS serviceOrderId,          --service order details
            service_completed_date  AS serviceCompletedDate,
            service_order_part_id   AS serviceOrderPartId,      --parts
            part_id                 AS partId,
            item_number             AS itemNumber,
            item_description        AS itemDescription,
            qty_used                AS qtyUsed,
            unit_cost               AS unitCost,
            unit_price              AS unitPrice,
            line_total              AS lineTotal,
            order_total             AS orderTotal
        FROM get_customer_service_report(:customerId)
        """, nativeQuery = true)
    List<CustomerServiceReportProjection> findCustomerServiceReport_Procedure(@Param("customerId") Long customerId);
}

