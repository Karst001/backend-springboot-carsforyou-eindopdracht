package nl.carsforyou.garage.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;


@Entity
@Table(name = "customers")
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @NotBlank
    @Column(name = "first_name")
    private String firstName;
    @NotBlank
    @Column(name = "last_name")
    private String lastName;
    @NotBlank
    private String address;

    @Column(name = "zip_code")
    private String zipCode;
    @Column(name = "city")
    private String city;
    @Column(name = "country")
    private String country;

    @NotBlank
    @Column(name = "telephone_number")
    private String telephoneNumber;

    @Column(name = "email_address", nullable = false, unique = true)    //unique because it's the key between Customers and Users
    private String emailAddress;

    @Column(name = "user_id", nullable = true, insertable = false, updatable = false)   //nullable because a customer may not be a user like walk-in customers do not visit via web-api
    private Long userId;
    @Column(name = "newsletter_signup_date")
    private LocalDateTime newsletterSignupDate;


    //one-to-one relation (owning side):
    //this side has the FK column (customers.user_id) so it is the owner of this relation
    //Optional=true is used because walk-in customers may not have a User account, they did not use the API
    //unique=true because the database diagram shows a one-to-one mapping, a user can't belong to multiple customers).
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", unique = true)
    private UserEntity user;

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

    //the base getters/setters
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

    // added getters/setters for the relational table User
    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
