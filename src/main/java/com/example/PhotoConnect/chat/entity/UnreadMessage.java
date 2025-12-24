package com.example.PhotoConnect.chat.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "unread_messages")
public class UnreadMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private long unreadCount;
}
