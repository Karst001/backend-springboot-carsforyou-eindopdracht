package nl.carsforyou.garage.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;


@Entity
@Table(name = "customers")
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long customerId;

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String address;

    private String zipCode;
    private String city;
    private String country;

    @NotBlank
    private String telephoneNumber;

    @Column(name = "email_address", nullable = false, unique = true)    //unique because it's the key between Customers and Users
    private String emailAddress;

    @Column(name = "user_id", nullable = true)                          //nullable because a customer may not be a user like walk-in customers do not visit via web-api
    private Long userId;
    private LocalDateTime newsletterSignupDate;

    public CustomerEntity() {}

    public CustomerEntity(Long customerId, String firstName, String lastName, String address,
        String zipCode, String city, String country, String telephoneNumber, String emailAddress,
                          Long userId, LocalDateTime newsletterSignupDate) {

        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.zipCode = zipCode;
        this.city = city;
        this.country = country;
        this.telephoneNumber = telephoneNumber;
        this.emailAddress = emailAddress;
        this.userId = userId;
        this.newsletterSignupDate = newsletterSignupDate;
    }


    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
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
