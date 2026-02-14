package com.example.PhotoConnect.controller;

import com.example.PhotoConnect.model.*;
import com.example.PhotoConnect.service.AdminDashboardService;
import com.example.PhotoConnect.service.AdminManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
@RequiredArgsConstructor
public class AdminController {

    private final AdminDashboardService adminDashboardService;
    private final AdminManagementService adminManagementService;

    // ===== DASHBOARD STATS =====

    /**
     * Get admin dashboard statistics
     * GET /api/admin/dashboard/stats
     */
    @GetMapping("/dashboard/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = adminDashboardService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }

    // ===== PHOTOGRAPHER VERIFICATION =====

    /**
     * Get pending photographer verifications
     * GET /api/admin/photographers/pending
     */
    @GetMapping("/photographers/pending")
    public ResponseEntity<List<PhotographerProfile>> getPendingVerifications() {
        List<PhotographerProfile> pending = adminManagementService.getPendingVerifications();
        return ResponseEntity.ok(pending);
    }

    /**
     * Approve photographer profile
     * POST /api/admin/photographers/{id}/approve
     */
    @PostMapping("/photographers/{id}/approve")
    public ResponseEntity<?> approvePhotographer(@PathVariable Long id) {
        try {
            PhotographerProfile profile = adminManagementService.approvePhotographer(id);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Photographer approved successfully",
                "profile", profile
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    /**
     * Reject photographer profile
     * POST /api/admin/photographers/{id}/reject
     */
    @PostMapping("/photographers/{id}/reject")
    public ResponseEntity<?> rejectPhotographer(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> request) {
        try {
            String reason = request != null ? request.get("reason") : "Not specified";
            PhotographerProfile profile = adminManagementService.rejectPhotographer(id, reason);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Photographer rejected",
                "profile", profile
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // ===== BOOKING MANAGEMENT =====

    /**
     * Override booking status
     * PUT /api/admin/bookings/{id}/status
     */
    @PutMapping("/bookings/{id}/status")
    public ResponseEntity<?> overrideBookingStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        try {
            String statusStr = request.get("status");
            BookingStatus newStatus = BookingStatus.valueOf(statusStr);
            Booking booking = adminManagementService.overrideBookingStatus(id, newStatus);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Booking status updated",
                "booking", booking
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    /**
     * Cancel booking by admin
     * DELETE /api/admin/bookings/{id}
     */
    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<?> cancelBooking(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> request) {
        try {
            String reason = request != null ? request.get("reason") : "Administrative cancellation";
            adminManagementService.cancelBookingByAdmin(id, reason);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Booking cancelled successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // ===== REVIEW MODERATION =====

    /**
     * Get all reviews (for moderation)
     * GET /api/admin/reviews
     */
    @GetMapping("/reviews")
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = adminManagementService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    /**
     * Delete review
     * DELETE /api/admin/reviews/{id}
     */
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<?> deleteReview(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> request) {
        try {
            String reason = request != null ? request.get("reason") : "Violated community guidelines";
            adminManagementService.deleteReview(id, reason);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Review deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // ===== REPORT GENERATION =====

    /**
     * Generate bookings report (CSV)
     * GET /api/admin/reports/bookings
     */
    @GetMapping("/reports/bookings")
    public ResponseEntity<?> generateBookingsReport() {
        try {
            String filePath = adminManagementService.generateBookingsReport();
            File file = new File(filePath);
            
            Resource resource = new FileSystemResource(file);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                            "attachment; filename=\"" + file.getName() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Generate payments report (CSV)
     * GET /api/admin/reports/payments
     */
    @GetMapping("/reports/payments")
    public ResponseEntity<?> generatePaymentsReport() {
        try {
            String filePath = adminManagementService.generatePaymentsReport();
            File file = new File(filePath);
            
            Resource resource = new FileSystemResource(file);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                            "attachment; filename=\"" + file.getName() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Generate users report (CSV)
     * GET /api/admin/reports/users
     */
    @GetMapping("/reports/users")
    public ResponseEntity<?> generateUsersReport() {
        try {
            String filePath = adminManagementService.generateUsersReport();
            File file = new File(filePath);
            
            Resource resource = new FileSystemResource(file);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                            "attachment; filename=\"" + file.getName() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
