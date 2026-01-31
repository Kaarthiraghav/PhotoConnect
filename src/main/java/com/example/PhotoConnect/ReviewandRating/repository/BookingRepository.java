package com.example.PhotoConnect.ReviewandRating.repository;

import com.example.PhotoConnect.ReviewandRating.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Standard JpaRepository methods handle findById, which you need for validation.
}