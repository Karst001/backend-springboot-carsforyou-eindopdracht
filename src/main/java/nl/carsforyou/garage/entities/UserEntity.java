package nl.carsforyou.garage.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "email_address", nullable = false)
    private String emailAddress;

    @Column(name = "signup_date", nullable = false)
    private LocalDateTime signupDate;

    @Column(name = "last_signin_date")
    private LocalDateTime lastSigninDate;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "user_role", nullable = false)
    private String userRole;

    //As per the database diagram, there are two relations for the user table 1) Customer and 2) Appointments
    //1)
    //A one-to-one relation, a user can have at most 1 customer profile
    //The FK resides in the "customers" table (customers.user_id), therefore this side uses mappedBy="user"
    //Added @JsonIgnore, prevents infinite recursion if an entity is serialized to JSON.
    //Even though I already use DTOs, it prevents accidental issues
    @JsonIgnore
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private CustomerEntity customer;

    //2)
    //One-to-many relation, one user can create many appointments.
    //The FK resides in appointments.created_by_user_id (the owner is AppointmentEntity).
    @JsonIgnore
    @OneToMany(mappedBy = "createdByUser", fetch = FetchType.LAZY)
    private List<AppointmentEntity> createdAppointments = new ArrayList<>();

    public UserEntity() {}

    public UserEntity(Long userId, String emailAddress, LocalDateTime signupDate, LocalDateTime lastSigninDate, String passwordHash, String userRole) {

        this.userId = userId;
        this.emailAddress = emailAddress;
        this.signupDate = signupDate;
        this.lastSigninDate = lastSigninDate;
        this.passwordHash = passwordHash;
        this.userRole = userRole;
    }

    //base getters/setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public LocalDateTime getSignupDate() {
        return signupDate;
    }

    public void setSignupDate(LocalDateTime signupDate) {
        this.signupDate = signupDate;
    }

    public LocalDateTime getLastSigninDate() {
        return lastSigninDate;
    }

    public void setLastSigninDate(LocalDateTime lastSigninDate) {
        this.lastSigninDate = lastSigninDate;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    //added getters/setters for the relational table Customer
    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public List<AppointmentEntity> getCreatedAppointments() {
        return createdAppointments;
    }

    public void setCreatedAppointments(List<AppointmentEntity> createdAppointments) {
        this.createdAppointments = createdAppointments;
    }
}
