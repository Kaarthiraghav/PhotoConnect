package com.example.PhotoConnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.PhotoConnect.model.ChatMessage;




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
