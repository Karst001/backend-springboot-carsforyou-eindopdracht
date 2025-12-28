// Mapper -> converts entities to DTOs
package nl.carsforyou.garage.mappers;

import nl.carsforyou.garage.dtos.ServiceOrder.ServiceOrderRequestDto;
import nl.carsforyou.garage.dtos.ServiceOrder.ServiceOrderResponseDto;
import nl.carsforyou.garage.entities.ServiceOrderEntity;
import org.springframework.stereotype.Component;

@Component
public class ServiceOrderDTOMapper implements EntityDtoMapper<ServiceOrderEntity, ServiceOrderRequestDto, ServiceOrderResponseDto> {
    @Override
    public ServiceOrderResponseDto mapToDto(ServiceOrderEntity serviceOrderEntity) {
        ServiceOrderResponseDto response = new ServiceOrderResponseDto();

        response.setServiceOrderId(serviceOrderEntity.getServiceOrderId());
        response.setServiceCompletedDate(serviceOrderEntity.getServiceCompletedDate());
        response.setVehicleId(serviceOrderEntity.getVehicleId());

        return response;
    }

    @Override
    public ServiceOrderEntity mapToEntity(ServiceOrderRequestDto serviceOrder) {
        ServiceOrderEntity entity = new ServiceOrderEntity();

        //set values
        entity.setServiceCompletedDate(serviceOrder.getServiceCompletedDate());
        entity.setVehicleId(serviceOrder.getVehicleId());

        return entity;
    }
}
