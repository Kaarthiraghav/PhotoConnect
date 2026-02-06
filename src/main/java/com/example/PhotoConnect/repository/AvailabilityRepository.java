package com.example.PhotoConnect.repository;

import com.example.PhotoConnect.model.AvailabilitySlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvailabilityRepository extends JpaRepository<AvailabilitySlot, Long> {
    List<AvailabilitySlot> findByPhotographerId(Long photographerId);
}

