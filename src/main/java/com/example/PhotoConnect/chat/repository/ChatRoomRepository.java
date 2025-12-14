package com.example.PhotoConnect.chat.repository;

import com.example.PhotoConnect.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository<ChatRoom> extends JpaRepository<ChatRoom, Long> {
}
