package com.example.LCProjectAPI.Models;

import jakarta.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Please enter event name.")
    private String eventName;

    @NotBlank(message = "Please provide event description.")
    @Size(min = 5, message = "Description must be at least 5 characters.")
    private String description;

    @NotNull(message = "Please enter event date.")
    private LocalDate eventDate;

    @NotNull(message = "Please enter event time.")
    private LocalTime eventTime;

    @NotBlank(message = "Event location must not be empty.")
    private String eventLocation;

    @NotBlank(message = "Event category must not be empty.")
    private String eventCategory;

    @NotNull(message = "Event price must not be null.")
    private Double eventPrice;

//    @Lob
//    @Column(name = "event_image", columnDefinition = "mediumBLOB")
//    private byte[] eventImage;

    private String filePath;



    private Integer zipCode;

    @NotBlank(message = "Approval status must not be empty.")
    private String approvalStatus = "Pending";

    public Event() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }

    public LocalTime getEventTime() { return eventTime; }
    public void setEventTime(LocalTime eventTime) { this.eventTime = eventTime; }

    public String getEventLocation() { return eventLocation; }
    public void setEventLocation(String eventLocation) { this.eventLocation = eventLocation; }

    public String getEventCategory() { return eventCategory; }
    public void setEventCategory(String eventCategory) { this.eventCategory = eventCategory; }

    public Double getEventPrice() { return eventPrice; }
    public void setEventPrice(Double eventPrice) { this.eventPrice = eventPrice; }

//    public byte[] getEventImage() { return eventImage; }
//    public void setEventImage(byte[] eventImage) { this.eventImage = eventImage; }
//
//    public String getImageMimeType() { return imageMimeType; }
//    public void setImageMimeType(String imageMimeType) { this.imageMimeType = imageMimeType; }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getZipCode() { return zipCode; }
    public void setZipCode(Integer zipCode) { this.zipCode = zipCode; }

    public String getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(String approvalStatus) { this.approvalStatus = approvalStatus; }
}
