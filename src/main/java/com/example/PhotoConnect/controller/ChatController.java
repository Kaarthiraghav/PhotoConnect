package com.example.PhotoConnect.controller;

import com.example.PhotoConnect.dto.ChatMessageDto;
import com.example.PhotoConnect.model.ChatMessage;
import com.example.PhotoConnect.model.ChatRoom;
import com.example.PhotoConnect.service.ChatService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // Create or get a chat room (Fixed the casting issue from your snippet)
    @PostMapping("/room")
    public ChatRoom createChatRoom(@RequestParam String name) {
        return chatService.getOrCreateChatRoom(Long.valueOf(name));
    }

    // Get messages for a chat room
    @GetMapping("/room/{roomId}/messages")
    public List<ChatMessage> getMessages(@PathVariable Long roomId) {
        return chatService.getMessagesForBooking(roomId);
    }

    // WebSocket: Send message to a general topic
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/chat")
    public ChatMessage sendMessage(
            @DestinationVariable Long bookingId,
            @Valid ChatMessageDto messageDto
    ) {
        return chatService.saveMessage(
                bookingId,
                messageDto.getSenderId(),
                messageDto.getContent()
        );
    }


     //NEW: Handle messages for a specific booking/room ID
     // The {bookingId} is extracted using @DestinationVariable
    @MessageMapping("/chat/{bookingId}")
    @SendTo("/topic/chat/{bookingId}")
    public ChatMessage sendBookingMessage(@DestinationVariable Long bookingId, @Payload ChatMessage message) {
        // Ensure the room exists before saving the message
        chatService.getOrCreateChatRoom(bookingId);

        return chatService.saveMessage(
                bookingId,
                message.getSenderId(),
                message.getContent()
        );
    }
    @PostMapping("/rooms/{roomId}/read")
    public ResponseEntity<Void> markRoomAsRead(
            @PathVariable Long roomId,
            @RequestParam String userId
    ) {
        chatService.markRoomAsRead(roomId, userId);
        return ResponseEntity.ok().build();
    }

    // Get chat history by booking ID
    @GetMapping("/messages/{bookingId}")
    public List<ChatMessage> getChatHistory(@PathVariable Long bookingId) {
        return chatService.getMessagesForBooking(bookingId);
    }

    // Mark messages as read for a booking
    @PostMapping("/messages/{bookingId}/read")
    public ResponseEntity<Void> markMessagesAsRead(
            @PathVariable Long bookingId,
            @RequestParam String userId
    ) {
        chatService.markMessagesAsRead(bookingId, userId);
        return ResponseEntity.ok().build();
    }

    // Get unread message count
    @GetMapping("/unread/{bookingId}")
    public long getUnreadCount(
            @PathVariable Long bookingId,
            @RequestParam String userId
    ) {
        return chatService.getUnreadCount(bookingId, userId);
    }

    // Mark messages as delivered
    @PostMapping("/messages/{bookingId}/delivered")
    public ResponseEntity<Void> markMessagesAsDelivered(
            @PathVariable Long bookingId,
            @RequestParam String userId
    ) {
        chatService.markMessagesAsDelivered(bookingId, userId);
        return ResponseEntity.ok().build();
    }
}