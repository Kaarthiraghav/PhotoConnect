package com.example.PhotoConnect.Notification.service;

import com.example.PhotoConnect.Notification.entity.Notification;
import com.example.PhotoConnect.Notification.entity.NotificationType;
import com.example.PhotoConnect.Notification.entity.UserRole;
import com.example.PhotoConnect.Notification.repo.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private GmailService gmailService;

    public void createNotification(Long userId, UserRole role, String title, String message, NotificationType type, String email) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setUserRole(role);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);

        notificationRepository.save(notification);

        gmailService.sendEmail(email, title, message);
    }

    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    public void markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public void notifyBookingCreated(Long bookingId, Long clientId, Long photographerId, String clientEmail, String photographerEmail) {
        // Client notification
        createNotification(clientId, UserRole.CLIENT,
                "Booking Created",
                "Your booking #" + bookingId + " has been created.",
                NotificationType.BOOKING, clientEmail);

        // Photographer notification
        createNotification(photographerId, UserRole.PHOTOGRAPHER,
                "New Booking Request",
                "You have a new booking request #" + bookingId,
                NotificationType.BOOKING, photographerEmail);
    }

    public void notifyPaymentReceived(Long bookingId, Long userId, String email) {
        createNotification(userId, UserRole.CLIENT,
                "Payment Received",
                "Your payment for booking #" + bookingId + " has been marked as PAID.",
                NotificationType.PAYMENT, email);
    }

    public void notifyChatMessage(Long bookingId, Long senderId, Long receiverId, String message, String receiverEmail) {
        createNotification(receiverId, UserRole.CLIENT, // or PHOTOGRAPHER based on receiver
                "New Chat Message",
                "Message regarding booking #" + bookingId + ": " + message,
                NotificationType.CHAT, receiverEmail);
    }

    public void notifyReviewPosted(Long reviewId, Long photographerId, String email) {
        createNotification(photographerId, UserRole.PHOTOGRAPHER,
                "New Review Received",
                "You have a new review #" + reviewId,
                NotificationType.REVIEW, email);
    }

    // New method to mark all notifications as read for a user
    public void markAllAsRead(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        notifications.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(notifications);
    }

    public Page<Notification> getUserNotifications(Long userId, Pageable pageable) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    // NotificationService.java
    public void notifyAdminSystem(String title, String message, String adminEmail) {
        createNotification(null, UserRole.ADMIN, title, message, NotificationType.SYSTEM, adminEmail);
    }


}

