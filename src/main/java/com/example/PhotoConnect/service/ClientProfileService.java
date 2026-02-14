package com.example.PhotoConnect.service;

import com.example.PhotoConnect.model.ClientProfile;
import com.example.PhotoConnect.repository.ClientProfileRepository;

import org.springframework.stereotype.Service;

@Service
public class ClientProfileService {

    private final ClientProfileRepository repo;

    public ClientProfileService(ClientProfileRepository repo) {
        this.repo = repo;
    }
    @SuppressWarnings("null")    public ClientProfile save(ClientProfile profile) {
        return repo.save(profile);
    }

    public ClientProfile getByUserId(Long userId) {
        return repo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Client profile not found for user ID: " + userId));
    }
}

