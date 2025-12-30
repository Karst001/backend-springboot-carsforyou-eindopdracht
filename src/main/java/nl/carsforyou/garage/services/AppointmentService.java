// Service -> talks to repository
package nl.carsforyou.garage.services;

import nl.carsforyou.garage.dtos.appointment.AppointmentRequestDto;
import nl.carsforyou.garage.dtos.appointment.AppointmentResponseDto;
import nl.carsforyou.garage.entities.AppointmentEntity;
import nl.carsforyou.garage.helpers.DateValidationUtil;
import nl.carsforyou.garage.mappers.AppointmentDTOMapper;
import nl.carsforyou.garage.repositories.AppointmentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentDTOMapper appointmentDTOMapper;

    public AppointmentService(AppointmentRepository appointmentRepository, AppointmentDTOMapper appointmentDTOMapper) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentDTOMapper = appointmentDTOMapper;
    }

    public List<AppointmentResponseDto> getAllAppointments() {
        return appointmentDTOMapper.mapToDtoList(appointmentRepository.findAll());
    }

    public AppointmentResponseDto getAppointmentById(Long id) {
        AppointmentEntity appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Appointment with Id " + id + " was not found"));

        return appointmentDTOMapper.mapToDto(appointment);
    }

    public AppointmentResponseDto createAppointment(AppointmentRequestDto dto) {
        //store passed DTO in entityMapper
        AppointmentEntity entity = appointmentDTOMapper.mapToEntity(dto);

        // cannot be completed at creation time, only during update
        entity.setCompletedDate(null);

        //validate the appointment date, cannot be prior to today's date
        DateValidationUtil.validateNotInPast(
                dto.getAppointmentDate(),
                "appointmentDate"
        );

        //save to repository and return saved data
        AppointmentEntity saved = appointmentRepository.save(entity);
        return appointmentDTOMapper.mapToDto(saved);
    }

    public AppointmentResponseDto updateAppointment(Long id, AppointmentRequestDto dto) {
        AppointmentEntity existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Appointment with Id " + id + " was not found"));

        //validate the completed date using a helper
        DateValidationUtil.validateDateOrder(
                dto.getAppointmentDate(),
                dto.getCompletedDate(),
                "appointmentDate",
                "completedDate"
        );

        //if no errors, set values that were passed from dto
        existing.setAppointmentDate(dto.getAppointmentDate());
        existing.setReasonForVisit(dto.getReasonForVisit());
        existing.setCompletedDate(dto.getCompletedDate());
        existing.setVehicleId(dto.getVehicleId());

        //save to repository and return saved data
        AppointmentEntity saved = appointmentRepository.save(existing);
        return appointmentDTOMapper.mapToDto(saved);
    }

    public void deleteAppointment(Long id) {
        AppointmentEntity appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Appointment with Id " + id + " was not found"));

        //only delete if id did not throw an exception
        appointmentRepository.delete(appointment);
    }
}

