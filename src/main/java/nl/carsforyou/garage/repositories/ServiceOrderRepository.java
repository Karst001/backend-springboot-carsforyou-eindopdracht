// Repository -> returns entities
package nl.carsforyou.garage.repositories;

import nl.carsforyou.garage.entities.ServiceOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrderEntity, Long> {
    //check if there is a relation to Vehicle
    boolean existsByVehicle_VehicleId(Long customerId);

    //returns all service orders that belong to selected vehicle
    List<ServiceOrderEntity> findAllByVehicle_VehicleId(Long vehicleId);
}
