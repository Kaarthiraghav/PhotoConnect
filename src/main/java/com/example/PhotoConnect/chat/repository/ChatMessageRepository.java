package com.example.PhotoConnect.chat.repository;

import com.example.PhotoConnect.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository
        extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByChatRoomIdOrderByTimestampAsc(Long chatRoomId);

    List<ChatMessage> findByChatRoomIdAndSenderIdNotAndIsReadFalse(
            Long chatRoomId,
            String senderId
    );

    long countByChatRoomIdAndIsReadFalse(Long chatRoomId);
}
