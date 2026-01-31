package com.example.PhotoConnect.userprofile.model;

import jakarta.persistence.*;

@Entity
@Table(name = "photographer_packages")
public class PhotographerPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long photographerId;
    private String title;
    private String description;
    private double price;

    public PhotographerPackage() {}

    // getters & setters
}
