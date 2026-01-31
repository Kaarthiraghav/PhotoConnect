package com.example.PhotoConnect.userprofile.dto;

import java.time.LocalDate;

public class AvailabilityDTO {

    private Long photographerId;
    private LocalDate date;
    private boolean available;

    public AvailabilityDTO() {}

    public Long getPhotographerId() {
        return photographerId;
    }

    public void setPhotographerId(Long photographerId) {
        this.photographerId = photographerId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
