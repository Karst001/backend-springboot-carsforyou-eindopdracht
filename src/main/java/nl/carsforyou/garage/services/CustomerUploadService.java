package nl.carsforyou.garage.services;

import jakarta.transaction.Transactional;
import nl.carsforyou.garage.dtos.customer.CustomerUploadRequestDto;
import nl.carsforyou.garage.dtos.customer.CustomerUploadResponseDto;
import nl.carsforyou.garage.entities.CustomerEntity;
import nl.carsforyou.garage.entities.CustomerUploadEntity;
import nl.carsforyou.garage.mappers.CustomerUploadDTOMapper;
import nl.carsforyou.garage.repositories.CustomerRepository;
import nl.carsforyou.garage.repositories.CustomerUploadRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class CustomerUploadService {
    private final CustomerUploadRepository customerUploadRepository;
    private final CustomerUploadDTOMapper customerUploadDTOMapper;
    private final Path uploadRoot;
    private final CustomerRepository customerRepository;

    public CustomerUploadService(CustomerUploadRepository customerUploadRepository,
                                 CustomerUploadDTOMapper customerUploadDTOMapper, @Value("${garage.upload-dir}") String uploadDir, CustomerRepository customerRepository) {
        this.customerUploadRepository = customerUploadRepository;
        this.customerUploadDTOMapper = customerUploadDTOMapper;

        this.uploadRoot = Paths.get(uploadDir);
        this.customerRepository = customerRepository;
    }

    public List<CustomerUploadResponseDto> getAllCustomerUploads() {
        return customerUploadDTOMapper.mapToDtoList(customerUploadRepository.findAll());
    }

    public CustomerUploadResponseDto getCustomerUploadById(Long id) {
        CustomerUploadEntity customerUpload = customerUploadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "CustomerUpload with Id " + id + " was not found"));

        return customerUploadDTOMapper.mapToDto(customerUpload);
    }


    public CustomerUploadResponseDto createCustomerUpload(Long customerId, MultipartFile file) {
        //validate
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is required");
        }

        //store original file name and validate
        String originalName = file.getOriginalFilename();
        if (originalName == null || originalName.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File must have a name");
        }

        //For security, remove any path information
        String fileName = Path.of(originalName).getFileName().toString();

        //Extract extension
        String fileType = "";
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            fileType = fileName.substring(dotIndex + 1).toLowerCase();
        }

        //create folder filepath
        Path customerDir = uploadRoot.resolve("customers").resolve(customerId.toString());
        Path targetFile = customerDir.resolve(fileName);

        try {
            Files.createDirectories(customerDir);

            // Save file bytes to disk
            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not store file");
        }

        //create DTO for mapping
        CustomerUploadRequestDto dto = new CustomerUploadRequestDto();
        dto.setCustomerId(customerId);
        dto.setFileType(fileType);
        dto.setFileName(fileName);

        //Map the DTO to  entity
        CustomerUploadEntity entity = customerUploadDTOMapper.mapToEntity(dto);

        //Update server-generated fields
        entity.setUploadDate(LocalDateTime.now());
        entity.setFilePath(targetFile.toString());  //this stores like 'C:\temp\customers\1\{filename}'

        //create the relation between Customer and CustomerUpload
        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
        //set the relation
        entity.setCustomer(customer);

        //save
        CustomerUploadEntity saved = customerUploadRepository.save(entity);

        System.out.println("Saved uploadId = " + saved.getUploadId() + ", file: " + fileName);

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


    @Transactional
    public void deleteCustomerUpload(Long id) {
        CustomerUploadEntity customerUpload = customerUploadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "CustomerUpload with Id " + id + " was not found"));

        //First delete file from disk, if it fails do not delete from database
        String filePath = customerUpload.getFilePath();
        if (filePath != null && !filePath.isBlank()) {
            try {
                Files.deleteIfExists(Paths.get(filePath));
            } catch (IOException e) {
                // Decide your policy:
                // A) fail the request (recommended if you require disk consistency)
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Could not delete file from disk"
                );

                // B) OR log and continue (best-effort cleanup)
                // log.warn("Failed to delete file: {}", filePath, e);
            }
        }

        //no errors, delete file from database
        customerUploadRepository.delete(customerUpload);

        //check if folder is empty, if so, delete the folder too to prevent clutter
        Path dir = Paths.get(filePath).getParent();
        if (dir != null && Files.isDirectory(dir)) {
            try { Files.delete(dir); } catch (IOException ignored) {}
        }
    }
}


