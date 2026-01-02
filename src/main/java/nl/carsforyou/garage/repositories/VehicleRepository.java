// Repository -> returns entities
package nl.carsforyou.garage.repositories;

import nl.carsforyou.garage.entities.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {
    //check if there is a relation to Customers
    boolean existsByCustomer_CustomerId(Long customerId);

    //gets all vehicles belonging to a customer
    List<VehicleEntity> findAllByCustomer_CustomerId(Long customerId);
}
