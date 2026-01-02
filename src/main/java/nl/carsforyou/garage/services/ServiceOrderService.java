// Service -> talks to repository
package nl.carsforyou.garage.services;

import nl.carsforyou.garage.dtos.ServiceOrder.ServiceOrderRequestDto;
import nl.carsforyou.garage.dtos.ServiceOrder.ServiceOrderResponseDto;
import nl.carsforyou.garage.entities.ServiceOrderEntity;
import nl.carsforyou.garage.helpers.DateValidationUtil;
import nl.carsforyou.garage.mappers.ServiceOrderDTOMapper;
import nl.carsforyou.garage.repositories.ServiceOrderPartRepository;
import nl.carsforyou.garage.repositories.ServiceOrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static java.time.LocalDateTime.now;

@Service
public class ServiceOrderService {
    private final ServiceOrderRepository serviceOrderRepository;
    private final ServiceOrderDTOMapper serviceOrderDTOMapper;
    private final ServiceOrderPartRepository serviceOrderPartRepository;

    public ServiceOrderService(ServiceOrderRepository serviceOrderRepository, ServiceOrderDTOMapper serviceOrderDTOMapper, ServiceOrderPartRepository serviceOrderPartRepository) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.serviceOrderDTOMapper = serviceOrderDTOMapper;
        this.serviceOrderPartRepository = serviceOrderPartRepository;
    }

    public List<ServiceOrderResponseDto> getAllServiceOrders() {
        return serviceOrderDTOMapper.mapToDtoList(serviceOrderRepository.findAll());
    }

    public ServiceOrderResponseDto getServiceOrderById(Long id) {
        ServiceOrderEntity serviceOrder = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "ServiceOrder with Id " + id + " was not found"));

        return serviceOrderDTOMapper.mapToDto(serviceOrder);
    }

    public ServiceOrderResponseDto createServiceOrder(ServiceOrderRequestDto dto) {
        //store passed DTO in entityMapper
        ServiceOrderEntity entity = serviceOrderDTOMapper.mapToEntity(dto);

        //save to repository and return saved data
        ServiceOrderEntity saved = serviceOrderRepository.save(entity);
        return serviceOrderDTOMapper.mapToDto(saved);
    }

    public ServiceOrderResponseDto updateServiceOrder(Long id, ServiceOrderRequestDto dto) {
        ServiceOrderEntity existing = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "ServiceOrder with Id " + id + " was not found"));

        //validate the completed date using a helper
        DateValidationUtil.validateDateOrder(
                now(),
                dto.getServiceCompletedDate(),
                "currentDate",
                "completedDate"
        );

        //if no errors, set values that were passed from dto
        existing.setServiceCompletedDate(dto.getServiceCompletedDate());
        existing.setVehicleId(dto.getVehicleId());

        //save to repository and return saved data
        ServiceOrderEntity saved = serviceOrderRepository.save(existing);
        return serviceOrderDTOMapper.mapToDto(saved);
    }


    public void deleteServiceOrder(Long id) {
        ServiceOrderEntity serverOrder = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "ServiceOrder with Id " + id + " was not found"));

        //check if parts exists in ServiceOrderParts, if true do not delete
        if (serviceOrderPartRepository.existsByServiceOrder_ServiceOrderId(id)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot delete ServiceOrder " + id + " because it has parts. Delete the parts first."
            );
        }

        //only delete if id did not throw an exception
        serviceOrderRepository.delete(serverOrder);
    }
}
