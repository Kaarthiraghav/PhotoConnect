package com.example.PhotoConnect.chat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(
        name = "unread_messages",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"chat_room_id", "user_id"})
        }
)
public class UnreadMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "chat_room_id", nullable = false)
    private Long chatRoomId;

    @Setter
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Setter
    @Column(name = "unread_count", nullable = false)
    private int unreadCount = 0;

    // ---------- Constructors ----------

    public UnreadMessage() {
    }

    public UnreadMessage(Long chatRoomId, String userId) {
        this.chatRoomId = chatRoomId;
        this.userId = userId;
        this.unreadCount = 0;
    }

    // ---------- Getters & Setters ----------

    public void setChatRoom(ChatRoom chatRoom) {
    }
}
