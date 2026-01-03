// Mapper -> converts entities to DTOs
package nl.carsforyou.garage.mappers;

import nl.carsforyou.garage.dtos.appointment.AppointmentRequestDto;
import nl.carsforyou.garage.dtos.appointment.AppointmentResponseDto;
import nl.carsforyou.garage.entities.AppointmentEntity;
import org.springframework.stereotype.Component;

@Component
public class AppointmentDTOMapper implements EntityDtoMapper<AppointmentEntity, AppointmentRequestDto, AppointmentResponseDto> {
    @Override
    public AppointmentResponseDto mapToDto(AppointmentEntity appointmentEntity) {
        AppointmentResponseDto response = new AppointmentResponseDto();

        response.setAppointmentId(appointmentEntity.getAppointmentId());
        response.setAppointmentDate(appointmentEntity.getAppointmentDate());
        response.setReasonForVisit(appointmentEntity.getReasonForVisit());
        response.setCompletedDate(appointmentEntity.getCompletedDate());
        response.setCancelledDate(appointmentEntity.getCancelledDate());
        response.setVehicleId(appointmentEntity.getVehicleId());
        response.setCreatedByUserId(appointmentEntity.getCreatedByUserId());

        return response;
    }

    @Override
    public AppointmentEntity mapToEntity(AppointmentRequestDto appointment) {
        AppointmentEntity entity = new AppointmentEntity();

        //set values
        entity.setAppointmentDate(appointment.getAppointmentDate());
        entity.setReasonForVisit(appointment.getReasonForVisit());
        entity.setCompletedDate(appointment.getCompletedDate());
        entity.setVehicleId(appointment.getVehicleId());
        entity.setCreatedByUserId(appointment.getCreatedByUserId());

        return entity;
    }
}
