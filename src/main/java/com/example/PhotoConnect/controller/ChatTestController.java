package com.example.PhotoConnect.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat/test")
public class ChatTestController {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatTestController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/{roomId}")
    public void testSend(
            @PathVariable Long roomId,
            @RequestParam String message
    ) {
        messagingTemplate.convertAndSend(
                "/topic/chat/" + roomId,
                message
        );
    }
}
