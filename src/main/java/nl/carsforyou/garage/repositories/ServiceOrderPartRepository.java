// Repository -> returns entities
package nl.carsforyou.garage.repositories;

import nl.carsforyou.garage.entities.ServiceOrderPartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceOrderPartRepository extends JpaRepository<ServiceOrderPartEntity, Long> {
    //returns true if any line items exist for a given service order
    boolean existsByServiceOrder_ServiceOrderId(Long serviceOrderId);
}
