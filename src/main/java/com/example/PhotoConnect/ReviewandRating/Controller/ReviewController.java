package com.example.PhotoConnect.ReviewandRating.Controller;

import com.example.PhotoConnect.ReviewandRating.entity.Review;
import com.example.PhotoConnect.ReviewandRating.Service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/submit")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Review> create(@RequestBody Review review, Authentication authentication) {
        // SECURITY FIX: Extract the client ID from the authenticated user context
        // This prevents users from spoofing their ID in the request body
        // Note: Replace 'CustomUserDetails' with your actual UserDetails class name
        // Long authenticatedClientId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        // review.setClientId(authenticatedClientId);

        return ResponseEntity.ok(reviewService.submitReview(review));
    }

    @PatchMapping("/{id}/flag")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> flagReview(@PathVariable Long id) {
        reviewService.flagReview(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/photographer/{id}")
    public ResponseEntity<List<Review>> getByPhotographer(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewsForPhotographer(id));
    }

    @GetMapping("/photographer/{id}/stats")
    public ResponseEntity<Map<String, Object>> getStats(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getPhotographerStats(id));
    }
}