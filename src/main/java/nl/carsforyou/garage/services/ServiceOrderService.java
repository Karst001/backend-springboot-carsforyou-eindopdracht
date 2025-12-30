// Service -> talks to repository
package nl.carsforyou.garage.services;

import nl.carsforyou.garage.dtos.ServiceOrder.ServiceOrderRequestDto;
import nl.carsforyou.garage.dtos.ServiceOrder.ServiceOrderResponseDto;
import nl.carsforyou.garage.entities.ServiceOrderEntity;
import nl.carsforyou.garage.helpers.DateValidationUtil;
import nl.carsforyou.garage.mappers.ServiceOrderDTOMapper;
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


    public ServiceOrderService(ServiceOrderRepository serviceOrderRepository, ServiceOrderDTOMapper serviceOrderDTOMapper) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.serviceOrderDTOMapper = serviceOrderDTOMapper;
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

        //only delete if id did not throw an exception
        serviceOrderRepository.delete(serverOrder);
    }
}
