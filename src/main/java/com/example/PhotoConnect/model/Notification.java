package com.example.PhotoConnect.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
public class Notification {

    // Getters & Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private UserRole userRole; // CLIENT, PHOTOGRAPHER, ADMIN

    private String title;

    @Column(length = 500)
    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationType type; // BOOKING, PAYMENT, CHAT, REVIEW, SYSTEM

    private boolean isRead = false;

    private LocalDateTime createdAt = LocalDateTime.now();

}

