package com.example.PhotoConnect.chat.controller;


import com.example.PhotoConnect.chat.entity.ChatMessage;
import com.example.PhotoConnect.chat.entity.ChatRoom;
import com.example.PhotoConnect.chat.service.ChatService;
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

    //  Create a chat room
    @PostMapping("/room")
    public ChatRoom createChatRoom(@RequestParam String name) {
        return chatService.createChatRoom(name);
    }

    //  Get messages for a chat room
    @GetMapping("/room/{roomId}/messages")
    public List<ChatMessage> getMessages(@PathVariable Long roomId) {
        return chatService.getMessagesForRoom(roomId);
    }

    //  WebSocket: Send message
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/chat")
    public ChatMessage sendMessage(@Payload ChatMessage message) {
        return chatService.saveMessage(
                message.getChatRoom().getId(),
                message.getSenderId(),
                message.getContent()
        );
    }
}
