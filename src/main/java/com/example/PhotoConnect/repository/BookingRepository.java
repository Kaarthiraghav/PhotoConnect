package com.example.PhotoConnect.repository;

import com.example.PhotoConnect.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}

