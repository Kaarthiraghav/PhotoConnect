package com.example.PhotoConnect.chat.service;

import com.example.PhotoConnect.chat.entity.ChatMessage;
import com.example.PhotoConnect.chat.entity.ChatRoom;
import com.example.PhotoConnect.chat.entity.UnreadMessage;
import com.example.PhotoConnect.chat.event.ChatMessageEvent;
import com.example.PhotoConnect.chat.repository.ChatMessageRepository;
import com.example.PhotoConnect.chat.repository.ChatRoomRepository;
import com.example.PhotoConnect.chat.repository.UnreadMessageRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UnreadMessageRepository unreadMessageRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ChatService(ChatRoomRepository chatRoomRepository,
                       ChatMessageRepository chatMessageRepository,
                       UnreadMessageRepository unreadMessageRepository,
                       ApplicationEventPublisher eventPublisher) {

        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.unreadMessageRepository = unreadMessageRepository;
        this.eventPublisher = eventPublisher;
    }

    /* ---------------------------------------------------
       Chat Room
    --------------------------------------------------- */

    public ChatRoom getOrCreateChatRoom(Long bookingId) {
        return (ChatRoom) chatRoomRepository.findByBookingId(bookingId)
                .orElseGet(() -> {
                    ChatRoom room = new ChatRoom(bookingId);
                    room.setName("Booking-" + bookingId);
                    return chatRoomRepository.save(room);
                });
    }

    /* ---------------------------------------------------
       Message Send (unread increment)
    --------------------------------------------------- */

    public ChatMessage saveMessage(Long bookingId, String senderId, String content) {

        ChatRoom chatRoom = getOrCreateChatRoom(bookingId);

        ChatMessage message = new ChatMessage();
        message.setChatRoom(chatRoom);
        message.setSenderId(senderId);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        message.setDeliveryStatus(ChatMessage.DeliveryStatus.SENT);

        ChatMessage savedMessage = (ChatMessage) chatMessageRepository.save(message);

        // increment unread count
        incrementUnreadCount(chatRoom, senderId);

        // WebSocket / event
        eventPublisher.publishEvent(new ChatMessageEvent(savedMessage));

        return savedMessage;
    }

    /* ---------------------------------------------------
       Unread Logic
    --------------------------------------------------- */

    private void incrementUnreadCount(ChatRoom chatRoom, String senderId) {

        // Placeholder receiver logic (finalized on Day 18)
        String receiverId = "RECEIVER_USER";

        if (receiverId.equals(senderId)) return;

        UnreadMessage unread = unreadMessageRepository
                .findByChatRoomIdAndUserId(chatRoom.getId(), receiverId)
                .orElseGet(() -> {
                    UnreadMessage um = new UnreadMessage();
                    um.setChatRoom(chatRoom);
                    um.setUserId(receiverId);
                    um.setUnreadCount(0);
                    return um;
                });

        unread.setUnreadCount(unread.getUnreadCount() + 1);
        unreadMessageRepository.save(unread);
    }

    public void markRoomAsRead(Long roomId, String userId) {

        unreadMessageRepository
                .findByChatRoomIdAndUserId(roomId, userId)
                .ifPresent(unread -> {
                    unread.setUnreadCount(0);
                    unreadMessageRepository.save(unread);
                });
    }

    public long getUnreadCount(Long bookingId) {

        ChatRoom chatRoom = getOrCreateChatRoom(bookingId);

        return unreadMessageRepository
                .findByChatRoomIdAndUserId(chatRoom.getId(), "CURRENT_USER")
                .map(UnreadMessage::getUnreadCount)
                .orElse(Math.toIntExact(0L));
    }

    /* ---------------------------------------------------
       Message History
    --------------------------------------------------- */

    public List<ChatMessage> getMessagesForBooking(Long bookingId) {
        ChatRoom chatRoom = getOrCreateChatRoom(bookingId);
        return chatMessageRepository
                .findByChatRoomIdOrderByTimestampAsc(chatRoom.getId());
    }

    public List<ChatRoom> getAllChatRooms(String name) {
        return chatRoomRepository.findAll();
    }

    /* ---------------------------------------------------
       Delivery Status
    --------------------------------------------------- */

    public void markMessagesAsDelivered(Long bookingId, String currentUserId) {

        ChatRoom chatRoom = getOrCreateChatRoom(bookingId);

        List<ChatMessage> unreadMessages =
                chatMessageRepository.findByChatRoomIdAndSenderIdNotAndIsReadFalse(
                        chatRoom.getId(), currentUserId
                );

        unreadMessages.forEach(
                msg -> msg.setDeliveryStatus(ChatMessage.DeliveryStatus.DELIVERED)
        );

        chatMessageRepository.saveAll(unreadMessages);
    }

    public void markMessagesAsRead(Long bookingId, String userId) {

    }
}
