// Service -> talks to repository
package nl.carsforyou.garage.services;

import nl.carsforyou.garage.dtos.User.UserRequestDto;
import nl.carsforyou.garage.dtos.User.UserResponseDto;
import nl.carsforyou.garage.entities.AppointmentEntity;
import nl.carsforyou.garage.entities.UserEntity;
import nl.carsforyou.garage.mappers.UserDTOMapper;
import nl.carsforyou.garage.repositories.AppointmentRepository;
import nl.carsforyou.garage.repositories.CustomerRepository;
import nl.carsforyou.garage.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserDTOMapper userDTOMapper;
    private final CustomerRepository customerRepository;
    private final AppointmentRepository appointmentRepository;

    public UserService(UserRepository userRepository, UserDTOMapper userDTOMapper, CustomerRepository customerRepository, AppointmentRepository appointmentRepository) {
        this.userRepository = userRepository;
        this.userDTOMapper = userDTOMapper;
        this.customerRepository = customerRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public List<UserResponseDto> getAllUsers() {
        return userDTOMapper.mapToDtoList(userRepository.findAll());
    }

    public UserResponseDto getUserById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User with Id " + id + " was not found"));

        return userDTOMapper.mapToDto(user);
    }

    public UserResponseDto createUser(UserRequestDto dto) {
        //store passed DTO in entityMapper
        UserEntity entity = userDTOMapper.mapToEntity(dto);

        //save to repository and return saved data
        UserEntity saved = userRepository.save(entity);
        return userDTOMapper.mapToDto(saved);
    }

    public UserResponseDto updateUser(Long id, UserRequestDto dto) {
        UserEntity existing = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User with Id " + id + " was not found"));

        //if no errors, set values that were passed from dto
        existing.setEmailAddress(dto.getEmailAddress());
        existing.setSignupDate(dto.getSignupDate());
        existing.setLastSigninDate(dto.getLastSigninDate());
        existing.setUserRole(dto.getUserRole());

        //save to repository and return saved data
        UserEntity saved = userRepository.save(existing);
        return userDTOMapper.mapToDto(saved);
    }

    public void deleteUser(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User with Id " + id + " was not found"));

        //now we have relations between User <> Customer and User <> Appointment
        //if user gets deleted, the relation needs to be removed in these tables
        //Remove Customer <>User relation
        customerRepository.findByUser_UserId(id)
                .ifPresent(customer -> {
                    customer.setUser(null);                 //remove the relation
                    customerRepository.save(customer);
                }
        );

        //Remove Appointment <> User relation
        //load appointments with all available data for the {id}
        List<AppointmentEntity> appointments = appointmentRepository.findAllByCreatedByUser_UserId(id);
        //iterate through list and remove the relation
        for (AppointmentEntity a : appointments) {
            a.setCreatedByUser(null);
        }
        appointmentRepository.saveAll(appointments);

        //only delete if id did not throw an exception
        userRepository.delete(user);
    }
}
