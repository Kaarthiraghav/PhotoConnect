package com.example.PhotoConnect.booking.repository;

import com.example.PhotoConnect.booking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}

