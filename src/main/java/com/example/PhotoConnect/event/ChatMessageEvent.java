package com.example.PhotoConnect.event;

import com.example.PhotoConnect.model.ChatMessage;

public class ChatMessageEvent {

    private final ChatMessage message;

    public ChatMessageEvent(ChatMessage message) {
        this.message = message;
    }

    public ChatMessage getMessage() {
        return message;
    }
}
