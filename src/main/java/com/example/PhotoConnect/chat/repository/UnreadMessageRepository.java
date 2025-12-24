package com.example.PhotoConnect.chat.repository;

import com.example.PhotoConnect.chat.entity.UnreadMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnreadMessageRepository extends JpaRepository<UnreadMessage, Long> {

    Optional<UnreadMessage> findByChatRoomIdAndUserId(Long chatRoomId, String userId);
}
