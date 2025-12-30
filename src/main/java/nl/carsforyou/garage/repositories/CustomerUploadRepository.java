// Repository -> returns entities
package nl.carsforyou.garage.repositories;

import nl.carsforyou.garage.entities.CustomerUploadEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerUploadRepository extends JpaRepository<CustomerUploadEntity, Long> {
}
