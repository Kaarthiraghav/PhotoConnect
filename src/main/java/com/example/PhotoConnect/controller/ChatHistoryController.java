package com.example.PhotoConnect.controller;

import com.example.PhotoConnect.model.ChatMessage;
import com.example.PhotoConnect.service.ChatService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatHistoryController {

    private final ChatService chatService;

    public ChatHistoryController(ChatService chatService) {
        this.chatService = chatService;
    }

    // Fetch chat history by booking ID
    @GetMapping("/history/{bookingId}")
    public List<ChatMessage> getChatHistory(@PathVariable Long bookingId) {
        return chatService.getMessagesForBooking(bookingId);
    }

    @PostMapping("/read/{bookingId}")
    public void markAsRead(
            @PathVariable Long bookingId,
            @RequestParam String userId
    ) {
        chatService.markMessagesAsRead(bookingId, userId);
    }

    @GetMapping("/unread-count/{bookingId}")
    public long getUnreadCount(
            @PathVariable Long bookingId,
            @RequestParam String userId
    ) {
        return chatService.getUnreadCount(bookingId, userId);
    }

    @PostMapping("/delivered/{bookingId}")
    public void markAsDelivered(
            @PathVariable Long bookingId,
            @RequestParam String userId
    ) {
        chatService.markMessagesAsDelivered(bookingId, userId);
    }




}
