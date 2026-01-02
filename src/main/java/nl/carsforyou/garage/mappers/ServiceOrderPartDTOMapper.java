// Mapper -> converts entities to DTOs
package nl.carsforyou.garage.mappers;

import nl.carsforyou.garage.dtos.ServiceOrder.ServiceOrderPartRequestDto;
import nl.carsforyou.garage.dtos.ServiceOrder.ServiceOrderPartResponseDto;
import nl.carsforyou.garage.entities.ServiceOrderPartEntity;
import org.springframework.stereotype.Component;

@Component
public class ServiceOrderPartDTOMapper implements EntityDtoMapper<ServiceOrderPartEntity, ServiceOrderPartRequestDto, ServiceOrderPartResponseDto> {
    @Override
    public ServiceOrderPartResponseDto mapToDto(ServiceOrderPartEntity serviceOrderPartEntity) {
        ServiceOrderPartResponseDto response = new ServiceOrderPartResponseDto();

        response.setServiceOrderPartId(serviceOrderPartEntity.getServiceOrderPartId());
        response.setServiceOrderId(serviceOrderPartEntity.getServiceOrderId());
        response.setPartId(serviceOrderPartEntity.getPartId());
        response.setUnitCost(serviceOrderPartEntity.getUnitCost());
        response.setUnitPrice(serviceOrderPartEntity.getUnitPrice());
        response.setQtyUsed(serviceOrderPartEntity.getQtyUsed());

        return response;
    }

    @Override
    public ServiceOrderPartEntity mapToEntity(ServiceOrderPartRequestDto serviceOrderPart) {
        ServiceOrderPartEntity entity = new ServiceOrderPartEntity();

        //set values
        entity.setServiceOrderId(serviceOrderPart.getServiceOrderId());
        entity.setPartId(serviceOrderPart.getPartId());
        entity.setUnitCost(serviceOrderPart.getUnitCost());
        entity.setUnitPrice(serviceOrderPart.getUnitPrice());
        entity.setQtyUsed(serviceOrderPart.getQtyUsed());

        return entity;
    }
}
