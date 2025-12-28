// Repository -> returns entities
package nl.carsforyou.garage.repositories;

import nl.carsforyou.garage.entities.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<VehicleEntity, Integer> {
}
