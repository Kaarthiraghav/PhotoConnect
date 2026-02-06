package com.example.PhotoConnect.repository;

import com.example.PhotoConnect.model.PhotographerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotographerProfileRepository extends JpaRepository<PhotographerProfile, Long> {
    Optional<PhotographerProfile> findByUserId(Long userId);
}
