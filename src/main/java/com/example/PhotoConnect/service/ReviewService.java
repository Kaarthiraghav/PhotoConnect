package com.example.PhotoConnect.service;

import com.example.PhotoConnect.model.PhotographerProfile;
import com.example.PhotoConnect.model.Review;
import com.example.PhotoConnect.model.ReviewBooking;
import com.example.PhotoConnect.repository.PhotographerProfileRepository;
import com.example.PhotoConnect.repository.ReviewBookingRepository;
import com.example.PhotoConnect.repository.ReviewRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReviewService {

    @Autowired private ReviewRepository reviewRepository;
    @Autowired private ReviewBookingRepository bookingRepository;
    @Autowired private PhotographerProfileRepository profileRepository;

    @Transactional
    public Review submitReview(Review review) {
        // 1. BUSINESS LOGIC: Check if Booking exists and is COMPLETED
        ReviewBooking booking = bookingRepository.findById(review.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!"COMPLETED".equalsIgnoreCase(booking.getStatus())) {
            throw new RuntimeException("Reviews can only be submitted for completed bookings.");
        }

        // 2. SET METADATA
        review.setCreatedAt(LocalDateTime.now());
        review.setFlagged(false);
        Review savedReview = reviewRepository.save(review);

        // 3. AGGREGATE RATING: Update PhotographerProfile table
        updatePhotographerStats(review.getPhotographerId());

        return savedReview;
    }

    private void updatePhotographerStats(Long photographerId) {
        Double avg = reviewRepository.getAverageRating(photographerId);
        Long count = reviewRepository.countByPhotographerId(photographerId);

        PhotographerProfile profile = profileRepository.findByUserId(photographerId)
                .orElseThrow(() -> new RuntimeException("Photographer profile not found"));

        profile.setAverageRating((avg != null) ? Math.round(avg * 10.0) / 10.0 : 0.0);
        profile.setReviewCount(count != null ? count.intValue() : 0);
        profileRepository.save(profile);
    }

    public List<Review> getReviewsForPhotographer(Long photographerId) {
        return reviewRepository.findByPhotographerIdAndIsFlaggedFalseOrderByCreatedAtDesc(photographerId);
    }

    public Map<String, Object> getPhotographerStats(Long photographerId) {
        PhotographerProfile profile = profileRepository.findByUserId(photographerId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        Map<String, Object> stats = new HashMap<>();
        stats.put("averageRating", profile.getAverageRating());
        stats.put("totalReviews", profile.getReviewCount());
        return stats;
    }

    @Transactional
    public void flagReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        review.setFlagged(true);
        reviewRepository.save(review);

        // Recalculate stats after a review is hidden
        updatePhotographerStats(review.getPhotographerId());
    }
}