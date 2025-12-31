package nl.carsforyou.garage.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;


@Entity
@Table(name = "appointments")
public class AppointmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;

    private LocalDateTime appointmentDate;
    private String reasonForVisit;
    private LocalDateTime completedDate;

    @NotNull
    @Positive
    @Column(name = "vehicle_id")          //mandatory, cannot have appointment w/o vehicle_id
    private Long vehicleId;

    @Positive
    @Column(name = "created_by_user_id")  //mandatory to track who created the appointment, customer or staff
    private Long createdByUserId;

    public AppointmentEntity() {}

    public AppointmentEntity(Long appointmentId, LocalDateTime appointmentDate, String reasonForVisit,
                             LocalDateTime completedDate, Long vehicleId, Long createdByUserId) {

        this.appointmentId = appointmentId;
        this.appointmentDate = appointmentDate;
        this.reasonForVisit = reasonForVisit;
        this.completedDate = completedDate;
        this.vehicleId = vehicleId;
        this.createdByUserId = createdByUserId;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getReasonForVisit() {
        return reasonForVisit;
    }

    public void setReasonForVisit(String reasonForVisit) {
        this.reasonForVisit = reasonForVisit;
    }

    public LocalDateTime getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(LocalDateTime completedDate) {
        this.completedDate = completedDate;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Long getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(Long createdByUserId) {
        this.createdByUserId = createdByUserId;
    }
}
