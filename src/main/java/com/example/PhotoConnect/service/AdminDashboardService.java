package com.example.PhotoConnect.service;

import com.example.PhotoConnect.repository.UserRepository;
import com.example.PhotoConnect.repository.BookingRepository;
import com.example.PhotoConnect.repository.PhotographerProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final PhotographerProfileRepository photographerProfileRepository;

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

        // Calculate monthly revenue (simple example)
        double monthlyRevenue = calculateMonthlyRevenue();
        stats.put("monthlyRevenue", monthlyRevenue);

        // Pending photographer verifications
        long pendingVerifications = photographerProfileRepository.countByVerifiedFalse();
        stats.put("pendingVerifications", pendingVerifications);

        return stats;
    }

    private double calculateMonthlyRevenue() {
        // Simple calculation - you might need to adjust based on your Booking entity
        // If your Booking entity has a 'price' or 'amount' field
        // You'll need to check what field name you use
        return 0.0; // Placeholder - we'll fix this next
    }
}