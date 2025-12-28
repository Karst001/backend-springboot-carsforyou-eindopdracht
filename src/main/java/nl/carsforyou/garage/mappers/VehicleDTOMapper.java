// Mapper -> converts entities to DTOs
package nl.carsforyou.garage.mappers;

import nl.carsforyou.garage.dtos.Vehicle.VehicleRequestDto;
import nl.carsforyou.garage.dtos.Vehicle.VehicleResponseDto;
import nl.carsforyou.garage.entities.VehicleEntity;
import org.springframework.stereotype.Component;

@Component
public class VehicleDTOMapper implements EntityDtoMapper<VehicleEntity, VehicleRequestDto, VehicleResponseDto> {
    @Override
    public VehicleResponseDto mapToDto(VehicleEntity vehicleEntity) {
        VehicleResponseDto response = new VehicleResponseDto();

        response.setVehicleId(vehicleEntity.getVehicleId());
        response.setLicensePlate(vehicleEntity.getLicensePlate());
        response.setVinNumber(vehicleEntity.getVinNumber());
        response.setMake(vehicleEntity.getMake());
        response.setModel(vehicleEntity.getModel());
        response.setPaintColor(vehicleEntity.getPaintColor());
        response.setBodyStyle(vehicleEntity.getBodyStyle());
        response.setTrailerHitch(vehicleEntity.getTrailerHitch());
        response.setCostPrice(vehicleEntity.getCostPrice());
        response.setSalePrice(vehicleEntity.getSalePrice());
        response.setCustomerId(vehicleEntity.getCustomerId());

        return response;
    }

    @Override
    public VehicleEntity mapToEntity(VehicleRequestDto vehicle) {
        VehicleEntity entity = new VehicleEntity();

        //set values
        entity.setLicensePlate(vehicle.getLicensePlate());
        entity.setVinNumber(vehicle.getVinNumber());
        entity.setMake(vehicle.getMake());
        entity.setModel(vehicle.getModel());
        entity.setPaintColor(vehicle.getPaintColor());
        entity.setBodyStyle(vehicle.getBodyStyle());
        entity.setTrailerHitch(vehicle.getTrailerHitch());
        entity.setCostPrice(vehicle.getCostPrice());
        entity.setSalePrice(vehicle.getSalePrice());
        entity.setCustomerId(vehicle.getCustomerId());

        return entity;
    }
}
