package nl.carsforyou.garage.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "Users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(name = "email_address", nullable = false)
    private String emailAddress;
    @Column(name = "signup_date", nullable = false)
    private Date signupDate;
    private Date lastSigninDate;
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;
    @Column(name = "user_role", nullable = false)
    private String userRole;

    public UserEntity() {}

    public UserEntity(Long userId, String emailAddress, Date signupDate,
        Date lastSigninDate, String passwordHash, String userRole) {

        this.userId = userId;
        this.emailAddress = emailAddress;
        this.signupDate = signupDate;
        this.lastSigninDate = lastSigninDate;
        this.passwordHash = passwordHash;
        this.userRole = userRole;
    }

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

    public Date getSignupDate() {
        return signupDate;
    }

    public void setSignupDate(Date signupDate) {
        this.signupDate = signupDate;
    }

    public Date getLastSigninDate() {
        return lastSigninDate;
    }

    public void setLastSigninDate(Date lastSigninDate) {
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
}
