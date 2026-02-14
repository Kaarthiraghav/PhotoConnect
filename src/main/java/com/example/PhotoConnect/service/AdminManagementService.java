package com.example.PhotoConnect.service;

import com.example.PhotoConnect.model.*;
import com.example.PhotoConnect.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminManagementService {

    private static final Logger log = LoggerFactory.getLogger(AdminManagementService.class);

    private final PhotographerProfileRepository photographerProfileRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final NotificationService notificationService;
    private final EmailService emailService;

    @Value("${reports.storage.path:reports/}")
    private String reportsStoragePath;

    // ===== PHOTOGRAPHER VERIFICATION =====

    @Transactional
    public PhotographerProfile approvePhotographer(Long photographerProfileId) {
        PhotographerProfile profile = photographerProfileRepository.findById(photographerProfileId)
                .orElseThrow(() -> new RuntimeException("Photographer profile not found"));
        
        profile.setVerified(true);
        PhotographerProfile savedProfile = photographerProfileRepository.save(profile);

        // Send notification to photographer
        if (profile.getUserId() != null) {
            User photographer = userRepository.findById(profile.getUserId()).orElse(null);
            if (photographer != null) {
                emailService.sendEmail(
                    photographer.getEmail(),
                    "PhotoConnect - Profile Verified",
                    "Congratulations! Your photographer profile has been verified by our admin team. " +
                    "You can now receive bookings from clients."
                );
            }
        }

        log.info("Photographer profile {} verified by admin", photographerProfileId);
        return savedProfile;
    }

    @Transactional
    public PhotographerProfile rejectPhotographer(Long photographerProfileId, String reason) {
        PhotographerProfile profile = photographerProfileRepository.findById(photographerProfileId)
                .orElseThrow(() -> new RuntimeException("Photographer profile not found"));
        
        profile.setVerified(false);
        PhotographerProfile savedProfile = photographerProfileRepository.save(profile);

        // Send notification to photographer
        if (profile.getUserId() != null) {
            User photographer = userRepository.findById(profile.getUserId()).orElse(null);
            if (photographer != null) {
                emailService.sendEmail(
                    photographer.getEmail(),
                    "PhotoConnect - Profile Verification",
                    "Your photographer profile verification was not approved. " +
                    "Reason: " + (reason != null ? reason : "Not specified") + "\n\n" +
                    "Please update your profile and try again."
                );
            }
        }

        log.info("Photographer profile {} rejected by admin. Reason: {}", photographerProfileId, reason);
        return savedProfile;
    }

    public List<PhotographerProfile> getPendingVerifications() {
        return photographerProfileRepository.findAll().stream()
                .filter(p -> !p.isVerified())
                .toList();
    }

    // ===== BOOKING OVERRIDE =====

    @Transactional
    public Booking overrideBookingStatus(Long bookingId, BookingStatus newStatus) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        BookingStatus oldStatus = booking.getStatus();
        booking.setStatus(newStatus);
        Booking savedBooking = bookingRepository.save(booking);

        log.info("Admin overrode booking {} status from {} to {}", bookingId, oldStatus, newStatus);
        return savedBooking;
    }

    @Transactional
    public void cancelBookingByAdmin(Long bookingId, String reason) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        // Notify both client and photographer
        User client = userRepository.findById(booking.getClientId()).orElse(null);
        User photographer = userRepository.findById(booking.getPhotographerId()).orElse(null);

        if (client != null) {
            emailService.sendEmail(
                client.getEmail(),
                "PhotoConnect - Booking Cancelled",
                "Your booking #" + bookingId + " has been cancelled by admin.\n" +
                "Reason: " + (reason != null ? reason : "Administrative decision")
            );
        }

        if (photographer != null) {
            emailService.sendEmail(
                photographer.getEmail(),
                "PhotoConnect - Booking Cancelled",
                "Booking #" + bookingId + " has been cancelled by admin.\n" +
                "Reason: " + (reason != null ? reason : "Administrative decision")
            );
        }

        log.info("Admin cancelled booking {} with reason: {}", bookingId, reason);
    }

    // ===== REVIEW MODERATION =====

    @Transactional
    public void deleteReview(Long reviewId, String reason) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        
        reviewRepository.delete(review);
        log.info("Admin deleted review {} with reason: {}", reviewId, reason);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> getReportedReviews() {
        // Assuming you might add a 'reported' flag to Review model in the future
        // For now, return all reviews
        return reviewRepository.findAll();
    }

    // ===== REPORT GENERATION =====

    public String generateBookingsReport() throws IOException {
        File directory = new File(reportsStoragePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = "bookings_report_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";
        String filePath = reportsStoragePath + fileName;

        try (FileWriter writer = new FileWriter(filePath);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .withHeader("ID", "Client ID", "Photographer ID", "Event Date", "Status", "Paid"))) {

            List<Booking> bookings = bookingRepository.findAll();
            for (Booking booking : bookings) {
                csvPrinter.printRecord(
                        booking.getId(),
                        booking.getClientId(),
                        booking.getPhotographerId(),
                        booking.getEventDate() != null ? booking.getEventDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : "N/A",
                        booking.getStatus(),
                        booking.isPaid()
                );
            }

            log.info("Bookings report generated: {}", filePath);
            return filePath;
        }
    }

    public String generatePaymentsReport() throws IOException {
        File directory = new File(reportsStoragePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = "payments_report_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";
        String filePath = reportsStoragePath + fileName;

        try (FileWriter writer = new FileWriter(filePath);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .withHeader("ID", "Booking ID", "Amount", "Payment Method", "Status", "Transaction ID", "Paid At"))) {

            List<Payment> payments = paymentRepository.findAll();
            for (Payment payment : payments) {
                csvPrinter.printRecord(
                        payment.getId(),
                        payment.getBookingId(),
                        payment.getAmount(),
                        payment.getPaymentMethod(),
                        payment.getPaymentStatus(),
                        payment.getTransactionId(),
                        payment.getPaidAt() != null ? payment.getPaidAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : "N/A"
                );
            }

            log.info("Payments report generated: {}", filePath);
            return filePath;
        }
    }

    public String generateUsersReport() throws IOException {
        File directory = new File(reportsStoragePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = "users_report_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";
        String filePath = reportsStoragePath + fileName;

        try (FileWriter writer = new FileWriter(filePath);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .withHeader("ID", "Username", "Email", "Role", "Enabled", "Location"))) {

            List<User> users = userRepository.findAll();
            for (User user : users) {
                csvPrinter.printRecord(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRole() != null ? user.getRole().getName() : "N/A",
                        user.isEnabled(),
                        user.getLocation() != null ? user.getLocation() : "N/A"
                );
            }

            log.info("Users report generated: {}", filePath);
            return filePath;
        }
    }
}
