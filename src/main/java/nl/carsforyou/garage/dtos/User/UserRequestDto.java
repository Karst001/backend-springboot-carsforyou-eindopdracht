// Used to receive data from client during CRUD, contains fields that the client is allowed to send
package nl.carsforyou.garage.dtos.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class UserRequestDto {
    @NotNull
    @Email
    private String emailAddress;

    @NotNull
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotNull
    private String userRole;

    private LocalDateTime signupDate;
    private LocalDateTime lastSigninDate;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
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
}
