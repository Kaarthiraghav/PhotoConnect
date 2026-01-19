package com.example.PhotoConnect.chat.event;

import com.example.PhotoConnect.chat.entity.ChatMessage;

public class ChatMessageEvent {

    private final ChatMessage message;

    public ChatMessageEvent(ChatMessage message) {
        this.message = message;
    }

    public ChatMessage getMessage() {
        return message;
    }
}
