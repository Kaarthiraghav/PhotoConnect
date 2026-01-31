package com.example.PhotoConnect.userprofile.service;

import com.example.PhotoConnect.userprofile.model.ClientProfile;
import com.example.PhotoConnect.userprofile.repository.ClientProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientProfileService {

    private final ClientProfileRepository repo;

    public ClientProfileService(ClientProfileRepository repo) {
        this.repo = repo;
    }

    public ClientProfile save(ClientProfile profile) {
        return repo.save(profile);
    }

    public ClientProfile getByUserId(Long userId) {
        return repo.findByUserId(userId).orElse(null);
    }
}

