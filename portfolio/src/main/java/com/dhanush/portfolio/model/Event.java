package com.dhanush.portfolio.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "event_date")
    private LocalDate eventDate;

    @Column(name = "event_type")
    private String eventType;

    private String location;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "event_id")
    private List<Photo> photos = new ArrayList<>();

    @Column(name = "cloudinary_folder")
    private String cloudinaryFolder;

    @Column(name = "is_active")
    private boolean active = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (cloudinaryFolder == null && eventName != null) {
            cloudinaryFolder = "events/" +
                    eventName.toLowerCase().replaceAll("[^a-z0-9]", "_") +
                    "_" + System.currentTimeMillis();
        }
    }
}