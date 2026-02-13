package com.example.PhotoConnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.PhotoConnect.model.PhotographerProfiles;

import java.util.Optional;

public interface PhotographerProfilesRepository extends JpaRepository<PhotographerProfiles, Long> {
    Optional<PhotographerProfiles> findByUserId(Long userId);
}
