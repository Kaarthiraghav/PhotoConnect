package com.example.PhotoConnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.PhotoConnect.model.AvailabilitySlot;

import java.util.List;

public interface AvailabilityRepository extends JpaRepository<AvailabilitySlot, Long> {
    List<AvailabilitySlot> findByPhotographerId(Long photographerId);
}

