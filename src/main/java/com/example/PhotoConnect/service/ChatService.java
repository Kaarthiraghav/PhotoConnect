package com.example.PhotoConnect.service;

import com.example.PhotoConnect.event.ChatMessageEvent;
import com.example.PhotoConnect.model.ChatMessage;
import com.example.PhotoConnect.model.ChatRoom;
import com.example.PhotoConnect.model.UnreadMessage;
import com.example.PhotoConnect.repository.ChatMessageRepository;
import com.example.PhotoConnect.repository.ChatRoomRepository;
import com.example.PhotoConnect.repository.UnreadMessageRepository;

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
        return chatRoomRepository.findByBookingId(bookingId)
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

        ChatMessage savedMessage = chatMessageRepository.save(message);

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

        // TODO: Implement receiver logic to fetch actual receiver from booking or chat participants
        // For now, this method tracks unread messages for all users except sender
        // The actual receiver ID should be obtained from the booking details
        
        UnreadMessage unread = unreadMessageRepository
                .findByChatRoomIdAndUserId(chatRoom.getId(), senderId)
                .orElseGet(() -> {
                    UnreadMessage um = new UnreadMessage();
                    um.setChatRoom(chatRoom);
                    um.setUserId(senderId);
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

    public long getUnreadCount(Long bookingId, String userId) {

        ChatRoom chatRoom = getOrCreateChatRoom(bookingId);

        return unreadMessageRepository
                .findByChatRoomIdAndUserId(chatRoom.getId(), userId)
                .map(unread -> (long) unread.getUnreadCount())
                .orElse(0L);
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
        ChatRoom chatRoom = getOrCreateChatRoom(bookingId);
        
        List<ChatMessage> unreadMessages = chatMessageRepository
                .findByChatRoomIdAndSenderIdNotAndIsReadFalse(chatRoom.getId(), userId);
        
        unreadMessages.forEach(msg -> msg.setIsRead(true));
        chatMessageRepository.saveAll(unreadMessages);
        
        // Also mark room as read for this user
        markRoomAsRead(chatRoom.getId(), userId);
    }
}
