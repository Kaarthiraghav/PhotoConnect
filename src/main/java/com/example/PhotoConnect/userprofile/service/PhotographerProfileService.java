package com.example.PhotoConnect.userprofile.service;

import com.example.PhotoConnect.userprofile.model.PhotographerProfile;
import com.example.PhotoConnect.userprofile.repository.PhotographerProfileRepository;
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
        return repo.findByUserId(userId).orElse(null);
    }
}

