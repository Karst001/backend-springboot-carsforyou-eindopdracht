// Service -> talks to repository
package nl.carsforyou.garage.services;

import nl.carsforyou.garage.dtos.ServiceOrder.ServiceOrderPartRequestDto;
import nl.carsforyou.garage.dtos.ServiceOrder.ServiceOrderPartResponseDto;
import nl.carsforyou.garage.entities.AppointmentEntity;
import nl.carsforyou.garage.entities.ServiceOrderPartEntity;
import nl.carsforyou.garage.mappers.ServiceOrderPartDTOMapper;
import nl.carsforyou.garage.repositories.ServiceOrderPartRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ServiceOrderPartService {
    private final ServiceOrderPartRepository serviceOrderPartRepository;
    private final ServiceOrderPartDTOMapper serviceOrderPartDTOMapper;

    public ServiceOrderPartService(ServiceOrderPartRepository serviceOrderPartRepository, ServiceOrderPartDTOMapper serviceOrderPartDTOMapper) {
        this.serviceOrderPartRepository = serviceOrderPartRepository;
        this.serviceOrderPartDTOMapper = serviceOrderPartDTOMapper;
    }

    public List<ServiceOrderPartResponseDto> getAllAppointments() {
        return serviceOrderPartDTOMapper.mapToDtoList(serviceOrderPartRepository.findAll());
    }

    public ServiceOrderPartResponseDto getServiceOrderPartById(Long id) {
        ServiceOrderPartEntity serviceOrderPart = serviceOrderPartRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "ServiceOrderPart with Id " + id + " was not found"));

        return serviceOrderPartDTOMapper.mapToDto(serviceOrderPart);
    }

    public ServiceOrderPartResponseDto createAppointment(ServiceOrderPartRequestDto dto) {
        //store passed DTO in entityMapper
        ServiceOrderPartEntity entity = serviceOrderPartDTOMapper.mapToEntity(dto);

        //save to repository and return saved data
        ServiceOrderPartEntity saved = serviceOrderPartRepository.save(entity);
        return serviceOrderPartDTOMapper.mapToDto(saved);
    }

    public ServiceOrderPartResponseDto updateServiceOrderPart(Long id, ServiceOrderPartRequestDto dto) {
        ServiceOrderPartEntity existing = serviceOrderPartRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "ServiceOrderPart with Id " + id + " was not found"));

        //if no errors, set values that were passed from dto
        existing.setServiceId(dto.getServiceId());
        existing.setPartId(dto.getPartId());
        existing.setUnitCost(dto.getUnitCost());
        existing.setUnitPrice(dto.getUnitPrice());
        existing.setQtyUsed(dto.getQtyUsed());

        //save to repository and return saved data
        ServiceOrderPartEntity saved = serviceOrderPartRepository.save(existing);
        return serviceOrderPartDTOMapper.mapToDto(saved);
    }

    public void deleteServiceOrderPart(Long id) {
        ServiceOrderPartEntity serviceOrderPart = serviceOrderPartRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "ServiceOrderPart with Id " + id + " was not found"));

        //only delete if id did not throw an exception
        serviceOrderPartRepository.delete(serviceOrderPart);
    }
}
