package com.example.PhotoConnect.userprofile.repository;

import com.example.PhotoConnect.userprofile.model.AvailabilitySlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvailabilityRepository extends JpaRepository<AvailabilitySlot, Long> {
    List<AvailabilitySlot> findByPhotographerId(Long photographerId);
}

