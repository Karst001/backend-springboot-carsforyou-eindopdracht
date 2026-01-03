package nl.carsforyou.garage.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "customer_uploads")
public class CustomerUploadEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uploadId;

    @Column(name = "customer_id", nullable = false, insertable = false, updatable = false)
    private Long customerId;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "description")
    private String description;     //added so user can describe the uploaded file


    private LocalDateTime uploadDate;

    //as per database diagram, CustomerUploads (owner of the relation) is a many-to-one relation to Customers
    //So many uploads belong to one customer
    //The FK column is customer_uploads.customer_id
    //@JsonIgnore avoids infinite recursion if entities are ever serialized.
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private CustomerEntity customer;

    public CustomerUploadEntity() {}

    public CustomerUploadEntity(Long uploadId, Long customerId, String filePath,
        String fileType, String fileName, LocalDateTime uploadDate) {
        this.uploadId = uploadId;
        this.customerId = customerId;
        this.filePath = filePath;
        this.fileType = fileType;
        this.fileName = fileName;
        this.uploadDate = uploadDate;
    }

    //base getters/setters
    public Long getUploadId() {
        return uploadId;
    }

    public void setUploadId(Long uploadId) {
        this.uploadId = uploadId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //added to get Customer details
    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }
}
