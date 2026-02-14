package com.example.PhotoConnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.PhotoConnect.model.ChatRoom;

import java.util.Optional;

public interface ChatRoomRepository
        extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByBookingId(Long bookingId);
}
