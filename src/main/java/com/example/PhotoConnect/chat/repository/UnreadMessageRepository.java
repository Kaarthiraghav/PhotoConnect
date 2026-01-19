package com.example.PhotoConnect.chat.repository;

import com.example.PhotoConnect.chat.entity.UnreadMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UnreadMessageRepository
        extends JpaRepository<UnreadMessage, Long> {

    @Modifying
    @Transactional
    @Query("""
        update UnreadMessage u
        set u.unreadCount = u.unreadCount + 1
        where u.chatRoomId = :roomId
          and u.userId <> :senderId
    """)
    void incrementUnreadCount(
            @Param("roomId") Long roomId,
            @Param("senderId") String senderId
    );

    Optional<UnreadMessage> findByChatRoomIdAndUserId(Long id, String receiverId);
}
