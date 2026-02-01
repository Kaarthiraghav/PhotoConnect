package com.example.PhotoConnect.repository;

import com.example.PhotoConnect.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByClientId(Long clientId);
    List<Booking> findByPhotographerId(Long photographerId);}