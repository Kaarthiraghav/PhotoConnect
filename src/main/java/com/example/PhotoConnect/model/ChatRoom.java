package com.example.PhotoConnect.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "chat_rooms")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Optional display name (can be bookingId as string or custom name)
    private String name;

    // One chat room per booking
    @Column(nullable = false, unique = true)
    private Long bookingId;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatMessage> messages;

    // Required for JPA
    public ChatRoom() {}

    // Convenience constructor
    public ChatRoom(Long bookingId) {
        this.bookingId = bookingId;
    }
}
