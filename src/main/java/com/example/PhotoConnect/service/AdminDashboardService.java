package com.example.PhotoConnect.service;

import com.example.PhotoConnect.model.Payment;
import com.example.PhotoConnect.repository.UserRepository;
import com.example.PhotoConnect.repository.BookingRepository;
import com.example.PhotoConnect.repository.PhotographerProfileRepository;
import com.example.PhotoConnect.repository.PaymentRepository;
import com.example.PhotoConnect.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final PhotographerProfileRepository photographerProfileRepository;
    private final PaymentRepository paymentRepository;
    private final ReviewRepository reviewRepository;

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        // Get total users
        long totalUsers = userRepository.count();
        stats.put("totalUsers", totalUsers);

        // Get total photographers (count profiles)
        long totalPhotographers = photographerProfileRepository.count();
        stats.put("totalPhotographers", totalPhotographers);

        // Get verified photographers
        long verifiedPhotographers = photographerProfileRepository.countByVerifiedTrue();
        stats.put("verifiedPhotographers", verifiedPhotographers);

        // Get total bookings
        long totalBookings = bookingRepository.count();
        stats.put("totalBookings", totalBookings);

        // Get today's bookings
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        long todayBookings = bookingRepository.countByEventDateAfter(startOfDay);
        stats.put("todayBookings", todayBookings);

        // Calculate monthly revenue
        double monthlyRevenue = calculateMonthlyRevenue();
        stats.put("monthlyRevenue", monthlyRevenue);

        // Pending photographer verifications
        long pendingVerifications = photographerProfileRepository.countByVerifiedFalse();
        stats.put("pendingVerifications", pendingVerifications);

        // Total reviews
        long totalReviews = reviewRepository.count();
        stats.put("totalReviews", totalReviews);

        return stats;
    }

    private double calculateMonthlyRevenue() {
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        List<Payment> monthlyPayments = paymentRepository.findAll().stream()
                .filter(p -> p.getCreatedAt() != null && p.getCreatedAt().isAfter(startOfMonth))
                .filter(p -> "COMPLETED".equals(p.getPaymentStatus()))
                .toList();
        
        return monthlyPayments.stream()
                .mapToDouble(Payment::getAmount)
                .sum();
    }
}
