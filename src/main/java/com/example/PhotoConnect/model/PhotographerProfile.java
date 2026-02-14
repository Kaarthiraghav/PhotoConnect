package com.example.PhotoConnect.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "photographer_profiles")
@Data
public class PhotographerProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", unique = true)
    private Long userId;

    private boolean verified = false;

    private String studioName;

    private Double averageRating = 0.0; // Updated automatically by ReviewService
    private Integer reviewCount = 0; // Updated automatically by ReviewService
}