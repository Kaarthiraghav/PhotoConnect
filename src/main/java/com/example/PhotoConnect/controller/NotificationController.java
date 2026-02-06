package com.example.PhotoConnect.controller;

import com.example.PhotoConnect.model.Notification;
import com.example.PhotoConnect.security.UserPrincipal;
import com.example.PhotoConnect.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public List<Notification> getMyNotifications(@AuthenticationPrincipal UserPrincipal user) {
        return notificationService.getUserNotifications(user.getId());
    }

    @GetMapping("/unread-count")
    public long getUnreadCount(@AuthenticationPrincipal UserPrincipal user) {
        return notificationService.getUnreadCount(user.getId());
    }

    @PutMapping("/{id}/read")
    public void markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
    }


    @GetMapping("/page")
    public List<Notification> getNotificationsPage(
            @RequestParam int page,
            @RequestParam int size,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return notificationService.getUserNotifications(user.getId(), pageable).getContent();
    }

    @PutMapping("/mark-all-read")
    public void markAllAsRead(@AuthenticationPrincipal UserPrincipal user) {
        notificationService.markAllAsRead(user.getId());
    }

}

