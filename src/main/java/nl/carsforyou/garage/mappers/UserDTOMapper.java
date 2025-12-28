// Mapper -> converts entities to DTOs
package nl.carsforyou.garage.mappers;

import nl.carsforyou.garage.dtos.User.UserRequestDto;
import nl.carsforyou.garage.dtos.User.UserResponseDto;
import nl.carsforyou.garage.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserDTOMapper implements EntityDtoMapper<UserEntity, UserRequestDto, UserResponseDto> {
    @Override
    public UserResponseDto mapToDto(UserEntity serviceOrderPartEntity) {
        UserResponseDto response = new UserResponseDto();

        response.setUserId(serviceOrderPartEntity.getUserId());
        response.setEmailAddress(serviceOrderPartEntity.getEmailAddress());
        response.setSignupDate(serviceOrderPartEntity.getSignupDate());
        response.setLastSigninDate(serviceOrderPartEntity.getLastSigninDate());
        //passwordHash not included, we do not exposes this
        response.setUserRole(serviceOrderPartEntity.getUserRole());

        return response;
    }

    @Override
    public UserEntity mapToEntity(UserRequestDto user) {
        UserEntity entity = new UserEntity();

        //set values
        entity.setEmailAddress(user.getEmailAddress());
        entity.setSignupDate(user.getSignupDate());
        entity.setLastSigninDate(user.getLastSigninDate());
        entity.setUserRole(user.getUserRole());

        return entity;
    }
}
