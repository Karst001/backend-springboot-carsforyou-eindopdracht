// Mapper -> converts entities to DTOs
package nl.carsforyou.garage.mappers;

import nl.carsforyou.garage.dtos.customer.CustomerUploadRequestDto;
import nl.carsforyou.garage.dtos.customer.CustomerUploadResponseDto;
import nl.carsforyou.garage.entities.CustomerUploadEntity;
import org.springframework.stereotype.Component;

@Component
public class CustomerUploadDTOMapper implements EntityDtoMapper<CustomerUploadEntity, CustomerUploadRequestDto, CustomerUploadResponseDto> {
    @Override
    public CustomerUploadResponseDto mapToDto(CustomerUploadEntity customerUploadEntity) {
        CustomerUploadResponseDto response = new CustomerUploadResponseDto();

        response.setUploadId(customerUploadEntity.getUploadId());
        response.setCustomerId(customerUploadEntity.getCustomerId());
        response.setFilePath(customerUploadEntity.getFilePath());
        response.setFileType(customerUploadEntity.getFileType());
        response.setFileName(customerUploadEntity.getFileName());
        response.setUploadDate(customerUploadEntity.getUploadDate());

        return response;
    }

    @Override
    public CustomerUploadEntity mapToEntity(CustomerUploadRequestDto customerUpload) {
        CustomerUploadEntity entity = new CustomerUploadEntity();

        //set values
        entity.setCustomerId(customerUpload.getCustomerId());
        entity.setFileType(customerUpload.getFileType());
        entity.setFileName(customerUpload.getFileName());

        return entity;
    }
}
