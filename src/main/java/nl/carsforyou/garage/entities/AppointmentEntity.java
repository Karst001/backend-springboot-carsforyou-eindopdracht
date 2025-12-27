package nl.carsforyou.garage.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Appointments")
public class AppointmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;
    private String appointmentDate;
    private String reasonForVisit;
    private String completedDate;
    @Column(name = "vehicle_id", nullable = false)          //mandatory, cannot have appointment w/o vehicle_id
    private Long vehicleId;
    @Column(name = "created_by_user_id", nullable = false)  //mandatory to track who created the appointment, customer or staff
    private Long createdByUserId;

    public AppointmentEntity() {}

    public AppointmentEntity(Long appointmentId, String appointmentDate, String reasonForVisit,
        String completedDate, Long vehicleId, Long createdByUserId) {

        this.appointmentId = appointmentId;
        this.appointmentDate = appointmentDate;
        this.reasonForVisit = reasonForVisit;
        this.completedDate = completedDate;
        this.vehicleId = vehicleId;
        this.createdByUserId = createdByUserId;
    }

    public long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getReasonForVisit() {
        return reasonForVisit;
    }

    public void setReasonForVisit(String reasonForVisit) {
        this.reasonForVisit = reasonForVisit;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
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
