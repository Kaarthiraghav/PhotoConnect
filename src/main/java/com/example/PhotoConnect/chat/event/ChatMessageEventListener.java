package com.example.PhotoConnect.chat.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageEventListener {

    @EventListener
    public void handleChatMessage(ChatMessageEvent event) {
        // Later Dev 10 will connect this to Notifications service
        System.out.println("New chat message event triggered");
    }
}
