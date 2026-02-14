package com.example.PhotoConnect.service;

import com.example.PhotoConnect.model.PhotographerProfile;
import com.example.PhotoConnect.repository.PhotographerProfileRepository;

import org.springframework.stereotype.Service;

@Service
public class PhotographerProfileService {

    private final PhotographerProfileRepository repo;

    public PhotographerProfileService(PhotographerProfileRepository repo) {
        this.repo = repo;
    }

    public PhotographerProfile save(PhotographerProfile profile) {
        return repo.save(profile);
    }

    public PhotographerProfile getByUserId(Long userId) {
        return repo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Photographer profile not found for user ID: " + userId));
    }
}

