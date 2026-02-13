package com.example.PhotoConnect.service;

import com.example.PhotoConnect.model.PhotographerProfiles;
import com.example.PhotoConnect.repository.PhotographerProfilesRepository;

import org.springframework.stereotype.Service;

@Service
public class PhotographerProfileService {

    private final PhotographerProfilesRepository repo;

    public PhotographerProfileService(PhotographerProfilesRepository repo) {
        this.repo = repo;
    }

    public PhotographerProfiles save(PhotographerProfiles profile) {
        return repo.save(profile);
    }

    public PhotographerProfiles getByUserId(Long userId) {
        return repo.findByUserId(userId).orElse(null);
    }
}

