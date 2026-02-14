package com.example.PhotoConnect.controller;

import com.example.PhotoConnect.model.PhotographerProfile;
import com.example.PhotoConnect.model.User;
import com.example.PhotoConnect.repository.PhotographerProfileRepository;
import com.example.PhotoConnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.lang.NonNull;

import java.util.*;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/photographers")
@CrossOrigin
public class PhotographersController {

    @Autowired
    private PhotographerProfileRepository photographerProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/top")
    public List<Map<String, Object>> topPhotographers() {
        return photographerProfileRepository.findAll().stream()
                .sorted((a, b) -> Double.compare(b.getAverageRating() != null ? b.getAverageRating() : 0.0, 
                                                   a.getAverageRating() != null ? a.getAverageRating() : 0.0))
                .limit(3)
                .map(this::photographerToMap)
                .collect(Collectors.toList());
    }

    @GetMapping
    public List<Map<String, Object>> allPhotographers() {
        return photographerProfileRepository.findAll().stream()
                .map(this::photographerToMap)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Map<String, Object> getPhotographerById(@PathVariable @NonNull Long id) {
        Objects.requireNonNull(id);
        PhotographerProfile profile = photographerProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Photographer not found"));
        return photographerToMap(profile);
    }

    @GetMapping("/user/{userId}")
    public Map<String, Object> getPhotographerByUserId(@PathVariable @NonNull Long userId) {
        Objects.requireNonNull(userId);
        PhotographerProfile profile = photographerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Photographer profile not found"));
        return photographerToMap(profile);
    }

    private Map<String, Object> photographerToMap(PhotographerProfile profile) {
        Map<String, Object> m = new HashMap<>();
        
        // Handle null userId gracefully
        User user = null;
        if (profile.getUserId() != null) {
            user = userRepository.findById(profile.getUserId()).orElse(null);
        }
        
        m.put("id", profile.getId());
        m.put("userId", profile.getUserId());
        m.put("studioName", profile.getStudioName() != null ? profile.getStudioName() : "");
        m.put("name", user != null ? user.getUsername() : "Unknown");
        m.put("email", user != null ? user.getEmail() : "");
        m.put("averageRating", profile.getAverageRating() != null ? profile.getAverageRating() : 0.0);
        m.put("reviewCount", profile.getReviewCount() != null ? profile.getReviewCount() : 0);
        m.put("verified", profile.isVerified());
        m.put("profilePhoto", user != null && user.getProfilePhoto() != null ? user.getProfilePhoto() : "/images/default-avatar.png");
        m.put("location", user != null && user.getLocation() != null ? user.getLocation() : "Not specified");
        m.put("hourlyRate", user != null && user.getHourlyRate() != null ? user.getHourlyRate() : 0.0);
        
        return m;
    }
}
