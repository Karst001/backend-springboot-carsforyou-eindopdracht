// Service -> talks to repository
package nl.carsforyou.garage.services;

import nl.carsforyou.garage.dtos.appointment.AppointmentRequestDto;
import nl.carsforyou.garage.dtos.appointment.AppointmentResponseDto;
import nl.carsforyou.garage.dtos.customer.CustomerRequestDto;
import nl.carsforyou.garage.dtos.customer.CustomerResponseDto;
import nl.carsforyou.garage.entities.AppointmentEntity;
import nl.carsforyou.garage.entities.CustomerEntity;
import nl.carsforyou.garage.helpers.DateValidationUtil;
import nl.carsforyou.garage.mappers.CustomerDTOMapper;
import nl.carsforyou.garage.repositories.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerDTOMapper customerDTOMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerDTOMapper customerDTOMapper) {
        this.customerRepository = customerRepository;
        this.customerDTOMapper = customerDTOMapper;
    }

    public List<CustomerResponseDto> getAllCustomers() {
        return customerDTOMapper.mapToDtoList(customerRepository.findAll());
    }


    public CustomerResponseDto getCustomerById(Long id) {
        CustomerEntity customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Customer with Id " + id + " was not found"));

        return customerDTOMapper.mapToDto(customer);
    }

    public CustomerResponseDto createCustomer(CustomerRequestDto dto) {
        //store passed DTO in entityMapper
        CustomerEntity entity = customerDTOMapper.mapToEntity(dto);

        //save to repository and return saved data
        CustomerEntity saved = customerRepository.save(entity);
        return customerDTOMapper.mapToDto(saved);
    }


    public CustomerResponseDto updateCustomer(Long id, CustomerRequestDto dto) {
        CustomerEntity existing = customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Customer with Id " + id + " was not found"));

        //if no errors, set values that were passed from dto
        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setAddress(dto.getAddress());
        existing.setZipCode(dto.getZipCode());
        existing.setCity(dto.getCity());
        existing.setCountry(dto.getCountry());
        existing.setTelephoneNumber(dto.getTelephoneNumber());
        existing.setEmailAddress(dto.getEmailAddress());
        existing.setNewsletterSignupDate(dto.getNewsletterSignupDate());

        //save to repository and return saved data
        CustomerEntity saved = customerRepository.save(existing);
        return customerDTOMapper.mapToDto(saved);
    }

    public void deleteCustomer(Long id) {
        CustomerEntity customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Customer with Id " + id + " was not found"));

        //only delete if id did not throw an exception
        customerRepository.delete(customer);
    }
}
