package com.example.PhotoConnect.chat.event;

import com.example.PhotoConnect.chat.entity.ChatMessage;
import com.example.PhotoConnect.chat.repository.UnreadMessageRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageEventListener {

    @EventListener
    public void handleChatMessageEvent(ChatMessageEvent event) {

        ChatMessage message = event.getMessage();

        unreadMessageRepository.incrementUnreadCount(
                message.getChatRoom().getId(),
                message.getSenderId()
        );
    }

    private final UnreadMessageRepository unreadMessageRepository;

    public ChatMessageEventListener(UnreadMessageRepository unreadMessageRepository) {
        this.unreadMessageRepository = unreadMessageRepository;
    }



}
