package com.example.PhotoConnect.userprofile.repository;

import com.example.PhotoConnect.userprofile.model.PhotographerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotographerProfileRepository extends JpaRepository<PhotographerProfile, Long> {
    Optional<PhotographerProfile> findByUserId(Long userId);
}
