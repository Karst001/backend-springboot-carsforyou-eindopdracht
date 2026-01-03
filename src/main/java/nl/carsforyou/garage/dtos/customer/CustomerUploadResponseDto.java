// Used to return data back to client, contains read-only and calculated fields that are safe to return
package nl.carsforyou.garage.dtos.customer;

import java.time.LocalDateTime;

public class CustomerUploadResponseDto {
    private Long uploadId;
    private Long customerId;
    private String filePath;
    private String fileType;
    private String fileName;                //this is just the reference to the file location, not the binary data
    private String description;
    private LocalDateTime uploadDate;

    public CustomerUploadResponseDto() {}

    public CustomerUploadResponseDto(Long uploadId, Long customerId, String filePath, String fileType, String fileName, LocalDateTime uploadDate) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
