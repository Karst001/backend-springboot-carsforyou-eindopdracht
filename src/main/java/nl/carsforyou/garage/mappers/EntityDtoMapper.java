package nl.carsforyou.garage.mappers;

import java.util.List;
import java.util.stream.Collectors;

//DTO-Mapper interface that provides a consistent mapper contract and can be used for all other entities
public interface EntityDtoMapper<Entity, Request, Response> {

    Response mapToDto(Entity entity);

    Entity mapToEntity(Request dto);

    //generic mapToDtoList is placed in interface, keeps code for all mappers cleaner
    default List<Response> mapToDtoList(List<Entity> entities) {
        return entities.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
