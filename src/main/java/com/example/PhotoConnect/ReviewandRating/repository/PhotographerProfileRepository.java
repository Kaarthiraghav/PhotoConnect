package com.example.PhotoConnect.ReviewandRating.repository;

import com.example.PhotoConnect.ReviewandRating.entity.PhotographerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PhotographerProfileRepository extends JpaRepository<PhotographerProfile, Long> {
    // Custom method to find the profile by the Photographer's User ID
    Optional<PhotographerProfile> findByUserId(Long userId);
}