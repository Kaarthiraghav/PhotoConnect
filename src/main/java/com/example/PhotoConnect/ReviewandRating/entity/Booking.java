package com.example.PhotoConnect.ReviewandRating.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "bookings")
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status; // Required to check if status is "COMPLETED"

    @Column(name = "photographer_id")
    private Long photographerId;

    @Column(name = "client_id")
    private Long clientId;
}