// Service -> talks to repository
package nl.carsforyou.garage.services;

import nl.carsforyou.garage.dtos.User.UserRequestDto;
import nl.carsforyou.garage.dtos.User.UserResponseDto;
import nl.carsforyou.garage.dtos.appointment.AppointmentRequestDto;
import nl.carsforyou.garage.dtos.appointment.AppointmentResponseDto;
import nl.carsforyou.garage.entities.AppointmentEntity;
import nl.carsforyou.garage.entities.UserEntity;
import nl.carsforyou.garage.helpers.DateValidationUtil;
import nl.carsforyou.garage.mappers.AppointmentDTOMapper;
import nl.carsforyou.garage.mappers.UserDTOMapper;
import nl.carsforyou.garage.repositories.AppointmentRepository;
import nl.carsforyou.garage.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserDTOMapper userDTOMapper;

    public UserService(UserRepository userRepository, UserDTOMapper userDTOMapper) {
        this.userRepository = userRepository;
        this.userDTOMapper = userDTOMapper;
    }

    public List<UserResponseDto> getAllUsers() {
        return userDTOMapper.mapToDtoList(userRepository.findAll());
    }

    public UserResponseDto getuserById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User with Id " + id + " was not found"));

        return userDTOMapper.mapToDto(user);
    }

    public UserResponseDto createAppointment(UserRequestDto dto) {
        //store passed DTO in entityMapper
        UserEntity entity = userDTOMapper.mapToEntity(dto);

        //save to repository and return saved data
        UserEntity saved = userRepository.save(entity);
        return userDTOMapper.mapToDto(saved);
    }

    public UserResponseDto updateUser(Long id, UserRequestDto dto) {
        UserEntity existing = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Appointment with Id " + id + " was not found"));

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

        //only delete if id did not throw an exception
        userRepository.delete(user);
    }
}
