// Service -> talks to repository
package nl.carsforyou.garage.services;

import jakarta.transaction.Transactional;
import nl.carsforyou.garage.dtos.customer.CustomerRequestDto;
import nl.carsforyou.garage.dtos.customer.CustomerResponseDto;
import nl.carsforyou.garage.entities.CustomerEntity;
import nl.carsforyou.garage.entities.CustomerUploadEntity;
import nl.carsforyou.garage.mappers.CustomerDTOMapper;
import nl.carsforyou.garage.repositories.CustomerRepository;
import nl.carsforyou.garage.repositories.CustomerUploadRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerDTOMapper customerDTOMapper;
    private final CustomerUploadRepository customerUploadRepository;

    public CustomerService(CustomerRepository customerRepository, CustomerDTOMapper customerDTOMapper, CustomerUploadRepository customerUploadRepository) {
        this.customerRepository = customerRepository;
        this.customerDTOMapper = customerDTOMapper;
        this.customerUploadRepository = customerUploadRepository;
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

    @Transactional
    public void deleteCustomer(Long id) {
        CustomerEntity customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Customer with Id " + id + " was not found"));

        //since i now have a relation between Customers and CustomerUploads i need to clean up the relations on delete
        //Also add logic that will delete the file from disk as it was stored on disk using uploadCreate
        //Get all uploads for this customer
        List<CustomerUploadEntity> uploads = customerUploadRepository.findAllByCustomer_CustomerId(id);

        //iterate through the uploads and delete file(s) from disk
        for (CustomerUploadEntity upload : uploads) {
            String filePath = upload.getFilePath();

            if (filePath != null && !filePath.isBlank()) {
                try {
                    Files.deleteIfExists(Paths.get(filePath));
                } catch (IOException e) {
                    // On error: stop and keep DB unchanged, because method is @Transactional, database deletes will be reversed
                    throw new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR, "Could not delete file from disk: " + filePath
                    );
                }
            }
        }

        //delete from repo for this {id}
        customerUploadRepository.deleteAll(uploads);

        //only delete if id did not throw an exception
        customerRepository.delete(customer);
    }
}
