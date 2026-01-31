package com.example.PhotoConnect.userprofile.dto;

public class PhotographerPackageDTO {

    private Long photographerId;
    private String title;
    private String description;
    private double price;

    public PhotographerPackageDTO() {}

    public Long getPhotographerId() {
        return photographerId;
    }

    public void setPhotographerId(Long photographerId) {
        this.photographerId = photographerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

