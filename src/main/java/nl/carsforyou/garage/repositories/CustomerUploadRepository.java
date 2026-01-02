// Repository -> returns entities
package nl.carsforyou.garage.repositories;

import nl.carsforyou.garage.entities.CustomerUploadEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerUploadRepository extends JpaRepository<CustomerUploadEntity, Long> {
    //this method returns all uploads belonging to a given customer, 'get all uploads where upload.customer.customerId = ??'
    List<CustomerUploadEntity> findAllByCustomer_CustomerId(Long customerId);
}
