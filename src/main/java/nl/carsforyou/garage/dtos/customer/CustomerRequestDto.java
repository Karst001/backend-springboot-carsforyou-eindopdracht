// Used to receive data from client during CRUD, contains fields that the client is allowed to send
package nl.carsforyou.garage.dtos.customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CustomerRequestDto {
    // customerId is generated in database

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    //not mandatory for a walk-in customer
    private String address;
    private String zipCode;
    private String city;
    private String telephoneNumber;
    private String country;

    @NotBlank
    private String emailAddress;

    private Long userId;
    private LocalDateTime newsletterSignupDate;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getNewsletterSignupDate() {
        return newsletterSignupDate;
    }

    public void setNewsletterSignupDate(LocalDateTime newsletterSignupDate) {
        this.newsletterSignupDate = newsletterSignupDate;
    }
}
