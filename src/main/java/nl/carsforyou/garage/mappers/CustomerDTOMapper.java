// Mapper -> converts entities to DTOs
package nl.carsforyou.garage.mappers;

import nl.carsforyou.garage.dtos.customer.CustomerRequestDto;
import nl.carsforyou.garage.dtos.customer.CustomerResponseDto;
import nl.carsforyou.garage.entities.CustomerEntity;
import org.springframework.stereotype.Component;

@Component
public class CustomerDTOMapper implements EntityDtoMapper<CustomerEntity, CustomerRequestDto, CustomerResponseDto> {
    @Override
    public CustomerResponseDto mapToDto(CustomerEntity customerEntity) {
        CustomerResponseDto response = new CustomerResponseDto();

        response.setCustomerId(customerEntity.getCustomerId());
        response.setFirstName(customerEntity.getFirstName());
        response.setLastName(customerEntity.getLastName());
        response.setAddress(customerEntity.getAddress());
        response.setZipCode(customerEntity.getZipCode());
        response.setCity(customerEntity.getCity());
        response.setCountry(customerEntity.getCountry());
        response.setTelephoneNumber(customerEntity.getTelephoneNumber());
        response.setEmailAddress(customerEntity.getEmailAddress());
        response.setUserId(customerEntity.getUserId());
        response.setNewsletterSignupDate(customerEntity.getNewsletterSignupDate());

        return response;
    }

    @Override
    public CustomerEntity mapToEntity(CustomerRequestDto customer) {
        CustomerEntity entity = new CustomerEntity();

        //set values
        entity.setFirstName(customer.getFirstName());
        entity.setLastName(customer.getLastName());
        entity.setAddress(customer.getAddress());
        entity.setZipCode(customer.getZipCode());
        entity.setCity(customer.getCity());
        entity.setCountry(customer.getCountry());
        entity.setTelephoneNumber(customer.getTelephoneNumber());
        entity.setEmailAddress(customer.getEmailAddress());
        entity.setUserId(customer.getUserId());
        entity.setNewsletterSignupDate(customer.getNewsletterSignupDate());

        return entity;
    }
}
