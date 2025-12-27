package nl.carsforyou.garage.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "customer_uploads")
public class CustomerUploadEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uploadId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_name")
    private String fileName;

    private LocalDateTime uploadDate;

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
}
