// Service -> talks to repository
package nl.carsforyou.garage.services;

import nl.carsforyou.garage.dtos.Vehicle.VehicleRequestDto;
import nl.carsforyou.garage.dtos.Vehicle.VehicleResponseDto;
import nl.carsforyou.garage.dtos.appointment.AppointmentRequestDto;
import nl.carsforyou.garage.dtos.appointment.AppointmentResponseDto;
import nl.carsforyou.garage.entities.AppointmentEntity;
import nl.carsforyou.garage.entities.VehicleEntity;
import nl.carsforyou.garage.helpers.DateValidationUtil;
import nl.carsforyou.garage.mappers.AppointmentDTOMapper;
import nl.carsforyou.garage.mappers.VehicleDTOMapper;
import nl.carsforyou.garage.repositories.AppointmentRepository;
import nl.carsforyou.garage.repositories.VehicleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final VehicleDTOMapper vehicleDTOMapper;

    public VehicleService(VehicleRepository vehicleRepository, VehicleDTOMapper vehicleDTOMapper) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleDTOMapper = vehicleDTOMapper;
    }

    public List<VehicleResponseDto> getAllVehicles() {
        return vehicleDTOMapper.mapToDtoList(vehicleRepository.findAll());
    }

    public VehicleResponseDto getVehicleById(Long id) {
        VehicleEntity vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Vehicle with Id " + id + " was not found"));

        return vehicleDTOMapper.mapToDto(vehicle);
    }

    public VehicleResponseDto createVehicle(VehicleRequestDto dto) {
        //store passed DTO in entityMapper
        VehicleEntity entity = vehicleDTOMapper.mapToEntity(dto);

        //save to repository and return saved data
        VehicleEntity saved = vehicleRepository.save(entity);
        return vehicleDTOMapper.mapToDto(saved);
    }

    public VehicleResponseDto updateVehicle(Long id, VehicleRequestDto dto) {
        VehicleEntity existing = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Vehicle with Id " + id + " was not found"));

        //if no errors, set values that were passed from dto
        existing.setLicensePlate(dto.getLicensePlate());
        existing.setVinNumber(dto.getVinNumber());
        existing.setMake(dto.getMake());
        existing.setModel(dto.getModel());

        existing.setPaintColor(dto.getPaintColor());
        existing.setBodyStyle(dto.getBodyStyle());
        existing.setTrailerHitch(dto.getTrailerHitch());
        //existing.setCostPrice(dto.getCostPrice()); do not include in response, sensitive info
        existing.setSalePrice(dto.getSalePrice());
        existing.setCustomerId(dto.getCustomerId());

        //save to repository and return saved data
        VehicleEntity saved = vehicleRepository.save(existing);
        return vehicleDTOMapper.mapToDto(saved);
    }


    public void deleteVehicle(Long id) {
        VehicleEntity appointment = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Vehicle with  Id " + id + " was not found"));

        //only delete if id did not throw an exception
        vehicleRepository.delete(appointment);
    }
}
