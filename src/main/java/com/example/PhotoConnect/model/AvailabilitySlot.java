package com.example.PhotoConnect.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "availability_slots")
public class AvailabilitySlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long photographerId;
    private LocalDate date;
    private boolean available;

    public AvailabilitySlot() {}

    // getters & setters
}

