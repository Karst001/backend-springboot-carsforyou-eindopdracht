// Service -> talks to repository
package nl.carsforyou.garage.services;

import nl.carsforyou.garage.dtos.ServiceOrder.ServiceOrderPartRequestDto;
import nl.carsforyou.garage.dtos.ServiceOrder.ServiceOrderPartResponseDto;
import nl.carsforyou.garage.entities.PartEntity;
import nl.carsforyou.garage.entities.ServiceOrderEntity;
import nl.carsforyou.garage.entities.ServiceOrderPartEntity;
import nl.carsforyou.garage.mappers.ServiceOrderPartDTOMapper;
import nl.carsforyou.garage.repositories.PartRepository;
import nl.carsforyou.garage.repositories.ServiceOrderPartRepository;
import nl.carsforyou.garage.repositories.ServiceOrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ServiceOrderPartService {
    private final ServiceOrderPartRepository serviceOrderPartRepository;
    private final ServiceOrderPartDTOMapper serviceOrderPartDTOMapper;
    private final PartRepository partRepository;
    private final ServiceOrderRepository serviceOrderRepository;

    public ServiceOrderPartService(ServiceOrderPartRepository serviceOrderPartRepository, ServiceOrderPartDTOMapper serviceOrderPartDTOMapper, PartRepository partRepository, ServiceOrderRepository serviceOrderRepository) {
        this.serviceOrderPartRepository = serviceOrderPartRepository;
        this.serviceOrderPartDTOMapper = serviceOrderPartDTOMapper;
        this.partRepository = partRepository;
        this.serviceOrderRepository = serviceOrderRepository;
    }

    public List<ServiceOrderPartResponseDto> getAllServiceOrderParts() {
        return serviceOrderPartDTOMapper.mapToDtoList(serviceOrderPartRepository.findAll());
    }

    public ServiceOrderPartResponseDto getServiceOrderPartById(Long id) {
        ServiceOrderPartEntity serviceOrderPart = serviceOrderPartRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "ServiceOrderPart with Id " + id + " was not found"));

        return serviceOrderPartDTOMapper.mapToDto(serviceOrderPart);
    }

    public ServiceOrderPartResponseDto createServiceOrderPart(ServiceOrderPartRequestDto dto) {
        //store passed DTO in entityMapper, here it sets unitCost/unitPrice/qtyUsed and  maybe serviceId
        ServiceOrderPartEntity entity = serviceOrderPartDTOMapper.mapToEntity(dto);

        //load Part and create relation, this writes the part_id needed for the relation
        PartEntity part = partRepository.findById(dto.getPartId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Part not found"));

        entity.setPart(part);  //the part_id will be added

        //attach ServiceOrder, this writes the service_order_id
        ServiceOrderEntity serviceOrder = serviceOrderRepository.findById(dto.getServiceOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Service order not found"));
        entity.setServiceOrder(serviceOrder);

        //save to repository and return saved data
        ServiceOrderPartEntity saved = serviceOrderPartRepository.save(entity);
        return serviceOrderPartDTOMapper.mapToDto(saved);
    }

    public ServiceOrderPartResponseDto updateServiceOrderPart(Long id, ServiceOrderPartRequestDto dto) {
        ServiceOrderPartEntity existing = serviceOrderPartRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "ServiceOrderPart with Id " + id + " was not found"));

        //if no errors, set values that were passed from dto
        existing.setServiceOrderId(dto.getServiceOrderId());
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
