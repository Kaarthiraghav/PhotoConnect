package com.example.PhotoConnect.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(1) @Max(5)
    @Column(nullable = false)
    private int rating;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String comment;

    @Column(name = "booking_id", unique = true, nullable = false)
    private Long bookingId;

    @Column(name = "photographer_id", nullable = false)
    private Long photographerId;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    private boolean isFlagged = false;
    private LocalDateTime createdAt = LocalDateTime.now();
}