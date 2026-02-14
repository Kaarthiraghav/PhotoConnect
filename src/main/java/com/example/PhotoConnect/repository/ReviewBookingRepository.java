package com.example.PhotoConnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.PhotoConnect.model.ReviewBooking;

@Repository
public interface ReviewBookingRepository extends JpaRepository<ReviewBooking, Long> {
    // Standard JpaRepository methods handle findById, which you need for validation.
}