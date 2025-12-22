package com.example.PhotoConnect.chat.controller;

import com.example.PhotoConnect.chat.entity.ChatMessage;
import com.example.PhotoConnect.chat.service.ChatService;
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
    public long getUnreadCount(@PathVariable Long bookingId) {
        return chatService.getUnreadCount(bookingId);
    }

    @PostMapping("/delivered/{bookingId}")
    public void markAsDelivered(
            @PathVariable Long bookingId,
            @RequestParam String userId
    ) {
        chatService.markMessagesAsDelivered(bookingId, userId);
    }




}
