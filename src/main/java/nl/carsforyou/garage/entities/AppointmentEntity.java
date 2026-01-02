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

    @Column(name = "appointment_date")
    private LocalDateTime appointmentDate;
    @Column(name = "reason_for_visit")
    private String reasonForVisit;
    @Column(name = "completed_date")
    private LocalDateTime completedDate;

    @NotNull
    @Positive
    @Column(name = "vehicle_id")          //mandatory, cannot have appointment w/o vehicle_id
    private Long vehicleId;

    @Positive
    @Column(name = "created_by_user_id", nullable = true, insertable=false, updatable=false)  //mandatory to track who created the appointment, customer or staff
    private Long createdByUserId;

    //As per the database diagram this is a many-to-one relation:
    //Many appointments can be created by one user
    //I am using optional = true because some appointments can be walk-in, not through the API
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "created_by_user_id", referencedColumnName = "user_id")
    private UserEntity createdByUser;

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

    //base getters/setters
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

    //set the relation to define who created the appointment
    public void setCreatedByUser(UserEntity createdByUser) {
        this.createdByUser = createdByUser;
    }
}
