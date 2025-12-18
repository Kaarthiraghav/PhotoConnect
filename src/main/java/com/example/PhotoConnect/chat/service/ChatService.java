package com.example.PhotoConnect.chat.service;

import com.example.PhotoConnect.chat.entity.ChatMessage;
import com.example.PhotoConnect.chat.entity.ChatRoom;
import com.example.PhotoConnect.chat.repository.ChatMessageRepository;
import com.example.PhotoConnect.chat.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ChatService(ChatRoomRepository chatRoomRepository,
                       ChatMessageRepository chatMessageRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    /**
     * Get existing chat room for a booking
     * or create one if it does not exist
     */
    public ChatRoom getOrCreateChatRoom(Long bookingId) {
        return (ChatRoom) chatRoomRepository.findByBookingId(bookingId)
                .orElseGet(() -> {
                    ChatRoom room = new ChatRoom(bookingId);
                    room.setName("Booking-" + bookingId);
                    return chatRoomRepository.save(room);
                });
    }

    /**
     * Save a new message to a booking-based chat room
     */
    public ChatMessage saveMessage(Long bookingId, String senderId, String content) {

        ChatRoom chatRoom = getOrCreateChatRoom(bookingId);

        ChatMessage message = new ChatMessage();
        message.setChatRoom(chatRoom);
        message.setSenderId(senderId);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());

        return (ChatMessage) chatMessageRepository.save(message);
    }

    /**
     * Fetch all messages for a booking
     */
    public List<ChatMessage> getMessagesForBooking(Long bookingId) {
        ChatRoom chatRoom = getOrCreateChatRoom(bookingId);
        return chatMessageRepository.findByChatRoomIdOrderByTimestampAsc(chatRoom.getId());
    }

    /**
     * (Optional) Admin / debug use
     */
    public List<ChatRoom> getAllChatRooms(String name) {
        return chatRoomRepository.findAll();
    }
}
