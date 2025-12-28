// Repository -> returns entities
package nl.carsforyou.garage.repositories;

import nl.carsforyou.garage.entities.ServiceOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrderEntity, Integer> {
}
