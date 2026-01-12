package com.example.PhotoConnect.Notification.entity;

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
    private com.example.PhotoConnect.Notification.entity.UserRole userRole; // CLIENT, PHOTOGRAPHER, ADMIN

    private String title;

    @Column(length = 500)
    private String message;

    @Enumerated(EnumType.STRING)
    private com.example.PhotoConnect.Notification.entity.NotificationType type; // BOOKING, PAYMENT, CHAT, REVIEW, SYSTEM

    private boolean isRead = false;

    private LocalDateTime createdAt = LocalDateTime.now();

}

