package com.example.PhotoConnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.PhotoConnect.model.ClientProfile;

import java.util.Optional;

public interface ClientProfileRepository extends JpaRepository<ClientProfile, Long> {
    Optional<ClientProfile> findByUserId(Long userId);
}

