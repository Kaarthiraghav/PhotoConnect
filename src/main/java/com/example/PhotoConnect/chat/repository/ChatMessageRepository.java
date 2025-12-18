package com.example.PhotoConnect.chat.repository;

import com.example.PhotoConnect.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository<ChatMessage> extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByChatRoomId(Long chatRoomId);
    List<ChatMessage> findByChatRoomIdOrderByTimestampAsc(Long chatRoomId);

}
