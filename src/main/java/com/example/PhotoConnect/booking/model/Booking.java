package com.example.booking_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clientId;
    private Long photographerId;

    private LocalDateTime eventDate;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private boolean paid;
}

