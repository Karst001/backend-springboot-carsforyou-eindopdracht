// Used to return data back to client, contains read-only and calculated fields that are safe to return
package nl.carsforyou.garage.dtos.customer;

import java.time.LocalDateTime;

public class CustomerResponseDto {
    private Long customerId;
    private String firstName;
    private String lastName;
    private String address;                         //this may be null for Walk-in customers
    private String zipCode;                         //this may be null for Walk-in customers
    private String city;                            //this may be null for Walk-in customers
    private String country;                         //this may be null for Walk-in customers
    private String telephoneNumber;                 //this may be null for Walk-in customers
    private String emailAddress;
    private Long userId;                            //this may be null for Walk-in customers
    private LocalDateTime newsletterSignupDate;     //this may be null for Walk-in customers

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
