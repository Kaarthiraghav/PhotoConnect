package com.example.PhotoConnect.chat.event;

import com.example.PhotoConnect.chat.entity.ChatMessage;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageEventListener {

    @EventListener
    public void handleChatMessageEvent(ChatMessageEvent event) {
        ChatMessage message = event.getMessage();

        // This is where real-time updates will be triggered
        // (WebSocket / notification layer in future days)

        System.out.println(
                "New message event received for room: "
                        + message.getChatRoom().getId()
        );
    }
}
