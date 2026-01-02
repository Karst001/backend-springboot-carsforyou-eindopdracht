// Service -> talks to repository
package nl.carsforyou.garage.services;

import nl.carsforyou.garage.dtos.ServiceOrder.ServiceOrderResponseDto;
import nl.carsforyou.garage.dtos.Vehicle.VehicleRequestDto;
import nl.carsforyou.garage.dtos.Vehicle.VehicleResponseDto;
import nl.carsforyou.garage.dtos.appointment.AppointmentRequestDto;
import nl.carsforyou.garage.dtos.appointment.AppointmentResponseDto;
import nl.carsforyou.garage.entities.AppointmentEntity;
import nl.carsforyou.garage.entities.CustomerEntity;
import nl.carsforyou.garage.entities.ServiceOrderEntity;
import nl.carsforyou.garage.entities.VehicleEntity;
import nl.carsforyou.garage.helpers.DateValidationUtil;
import nl.carsforyou.garage.mappers.AppointmentDTOMapper;
import nl.carsforyou.garage.mappers.ServiceOrderDTOMapper;
import nl.carsforyou.garage.mappers.VehicleDTOMapper;
import nl.carsforyou.garage.repositories.AppointmentRepository;
import nl.carsforyou.garage.repositories.CustomerRepository;
import nl.carsforyou.garage.repositories.ServiceOrderRepository;
import nl.carsforyou.garage.repositories.VehicleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final VehicleDTOMapper vehicleDTOMapper;
    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final ServiceOrderRepository serviceOrderRepository;
    private final ServiceOrderDTOMapper serviceOrderDTOMapper;

    public VehicleService(VehicleRepository vehicleRepository, VehicleDTOMapper vehicleDTOMapper, AppointmentRepository appointmentRepository, CustomerRepository customerRepository, ServiceOrderRepository serviceOrderRepository, ServiceOrderDTOMapper serviceOrderDTOMapper) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleDTOMapper = vehicleDTOMapper;
        this.appointmentRepository = appointmentRepository;
        this.customerRepository = customerRepository;
        this.serviceOrderRepository = serviceOrderRepository;
        this.serviceOrderDTOMapper = serviceOrderDTOMapper;
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


    public List<ServiceOrderResponseDto> getServiceOrdersForVehicle(Long vehicleId) {
        //check if Vehicle exists
        if (!vehicleRepository.existsById(vehicleId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Vehicle with Id " + vehicleId + " was not found");
        }

        //get the list of orders for this vehicle
        List<ServiceOrderEntity> orders = serviceOrderRepository.findAllByVehicle_VehicleId(vehicleId);

        //map tp DTO and return
        return orders.stream()
                .map(serviceOrderDTOMapper::mapToDto)
                .toList();
    }

    public VehicleResponseDto createVehicle(VehicleRequestDto dto) {
        //store passed DTO in entityMapper
        VehicleEntity entity = vehicleDTOMapper.mapToEntity(dto);

        //check and set relation to customer
        CustomerEntity customer = checkCustomer(dto.getCustomerId());
        entity.setCustomer(customer);

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

        //check and update relation to customer
        CustomerEntity customer = checkCustomer(dto.getCustomerId());
        existing.setCustomer(customer);

        //save to repository and return saved data
        VehicleEntity saved = vehicleRepository.save(existing);
        return vehicleDTOMapper.mapToDto(saved);
    }


    public void deleteVehicle(Long id) {
        VehicleEntity vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Vehicle with  Id " + id + " was not found"));

        //prevent delete if appointments exist for this Vehicle
        if (appointmentRepository.existsByVehicle_VehicleId(id)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot delete Vehicle " + id + " because it has appointments. Please delete them first."
            );
        }

        //only delete if id did not throw an exception
        vehicleRepository.delete(vehicle);
    }


    //private helper to check relation with Customers
    private CustomerEntity checkCustomer(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Customer not found"));
    }
}
