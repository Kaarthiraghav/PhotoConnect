package com.example.PhotoConnect.chat.event;

import com.example.PhotoConnect.chat.entity.ChatMessage;
import com.example.PhotoConnect.chat.repository.UnreadMessageRepository;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageEventListener {

    private final UnreadMessageRepository unreadMessageRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatMessageEventListener(
            UnreadMessageRepository unreadMessageRepository,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.unreadMessageRepository = unreadMessageRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleChatMessageEvent(ChatMessageEvent event) {

        ChatMessage message = event.getMessage();

        // increment unread count
        unreadMessageRepository.incrementUnreadCount(
                message.getChatRoom().getId(),
                message.getSenderId()
        );

        // broadcast message via WebSocket
        messagingTemplate.convertAndSend(
                "/topic/chat/" + message.getChatRoom().getId(),
                message
        );
    }
}
