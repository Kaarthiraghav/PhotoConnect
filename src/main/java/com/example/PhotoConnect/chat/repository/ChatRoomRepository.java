package com.example.PhotoConnect.chat.repository;

import com.example.PhotoConnect.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository
        extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByBookingId(Long bookingId);
}
