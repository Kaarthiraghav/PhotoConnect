package com.example.PhotoConnect.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private Set<String> roles;
    private boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // For photographers
    private boolean isPhotographer;
    private boolean photographerVerified;
    private String photographerSpecialization;

    // For clients
    private boolean isClient;
}