package com.example.PhotoConnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.PhotoConnect.model.PhotographerProfile;

import java.util.Optional;

@Repository
public interface PhotographerProfileRepository extends JpaRepository<PhotographerProfile, Long> {
    // Custom method to find the profile by the Photographer's User ID
    Optional<PhotographerProfile> findByUserId(Long userId);

    long countByVerifiedTrue();

    long countByVerifiedFalse();
}