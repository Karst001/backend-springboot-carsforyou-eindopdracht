// Used to return data back to client, contains read-only and calculated fields that are safe to return
package nl.carsforyou.garage.dtos.User;

import java.time.LocalDateTime;

public class UserResponseDto {
    private Long userId;
    private String emailAddress;
    private LocalDateTime signupDate;
    private LocalDateTime lastSigninDate;
    private String userRole;
    //passwordHash is not included in response as we validate the password in the backend


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

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
