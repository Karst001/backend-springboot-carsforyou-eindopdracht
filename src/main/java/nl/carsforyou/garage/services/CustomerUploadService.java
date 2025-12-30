package nl.carsforyou.garage.services;

import nl.carsforyou.garage.dtos.customer.CustomerUploadRequestDto;
import nl.carsforyou.garage.dtos.customer.CustomerUploadResponseDto;
import nl.carsforyou.garage.entities.CustomerUploadEntity;
import nl.carsforyou.garage.mappers.CustomerUploadDTOMapper;
import nl.carsforyou.garage.repositories.CustomerUploadRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CustomerUploadService {
    private final CustomerUploadRepository customerUploadRepository;
    private final CustomerUploadDTOMapper customerUploadDTOMapper;

    public CustomerUploadService(CustomerUploadRepository customerUploadRepository, CustomerUploadDTOMapper customerUploadDTOMapper) {
        this.customerUploadRepository = customerUploadRepository;
        this.customerUploadDTOMapper = customerUploadDTOMapper;
    }

    public List<CustomerUploadResponseDto> getAllCustomerUploads() {
        return customerUploadDTOMapper.mapToDtoList(customerUploadRepository.findAll());
    }

    public CustomerUploadResponseDto getAppointmentById(Long id) {
        CustomerUploadEntity customerUpload = customerUploadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "CustomerUpload with Id " + id + " was not found"));

        return customerUploadDTOMapper.mapToDto(customerUpload);
    }

    public CustomerUploadResponseDto createCustomerUpload(CustomerUploadRequestDto dto) {
        //store passed DTO in entityMapper
        CustomerUploadEntity entity = customerUploadDTOMapper.mapToEntity(dto);

        //save to repository and return saved data
        CustomerUploadEntity saved = customerUploadRepository.save(entity);
        return customerUploadDTOMapper.mapToDto(saved);
    }

    public CustomerUploadResponseDto updateCustomerUpload(Long id, CustomerUploadRequestDto dto) {
        CustomerUploadEntity existing = customerUploadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "CustomerUpload with Id " + id + " was not found"));

        //if no errors, set values that were passed from dto
        existing.setCustomerId(dto.getCustomerId());
        existing.setFileType(dto.getFileType());
        existing.setFileName(dto.getFileName());

        //getFilePath()) & getUploadDate()) do not exists, see CustomerUploadRequestDTO, these are server generated fields and do not
        //need an update, only set when creating an upload

        //save to repository and return saved data
        CustomerUploadEntity saved = customerUploadRepository.save(existing);
        return customerUploadDTOMapper.mapToDto(saved);
    }

    public void deleteCustomerUpload(Long id) {
        CustomerUploadEntity customerUpload = customerUploadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "CustomerUpload with Id " + id + " was not found"));

        //only delete if id did not throw an exception
        customerUploadRepository.delete(customerUpload);
    }
}


