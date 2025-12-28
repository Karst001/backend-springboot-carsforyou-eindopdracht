// Mapper -> converts entities to DTOs
package nl.carsforyou.garage.mappers;

import nl.carsforyou.garage.dtos.part.PartRequestDto;
import nl.carsforyou.garage.dtos.part.PartResponseDto;
import nl.carsforyou.garage.entities.PartEntity;
import org.springframework.stereotype.Component;

@Component
public class PartDTOMapper implements EntityDtoMapper<PartEntity, PartRequestDto, PartResponseDto>{
    @Override
    public PartResponseDto mapToDto(PartEntity partEntity) {
        PartResponseDto response = new PartResponseDto();
        response.setPartId(partEntity.getPartId());
        response.setItemNumber(partEntity.getItemNumber());
        response.setItemDescription(partEntity.getItemDescription());
        response.setQtyOnHand(partEntity.getQtyOnHand());
        response.setQtyOnOrder(partEntity.getQtyOnOrder());
        response.setUnitCost(partEntity.getUnitCost());
        response.setUnitPrice(partEntity.getUnitPrice());

        return response;
    }

    @Override
    public PartEntity mapToEntity(PartRequestDto part) {
        PartEntity entity = new PartEntity();

        //set values
        entity.setItemNumber(part.getItemNumber());
        entity.setItemDescription(part.getItemDescription());
        entity.setQtyOnHand(part.getQtyOnHand());
        entity.setQtyOnOrder(part.getQtyOnOrder());
        entity.setUnitCost(part.getUnitCost());
        entity.setUnitPrice(part.getUnitPrice());

        return entity;
    }
}
