package com.example.PhotoConnect.chat.service;

import com.example.PhotoConnect.chat.entity.ChatMessage;
import com.example.PhotoConnect.chat.entity.ChatRoom;
import com.example.PhotoConnect.chat.repository.ChatMessageRepository;
import com.example.PhotoConnect.chat.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ChatService(ChatRoomRepository chatRoomRepository, ChatMessageRepository chatMessageRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    //  Create a new chat room
    public ChatRoom createChatRoom(String roomName) {
        ChatRoom room = new ChatRoom();
        room.setName(roomName);
        return (ChatRoom) chatRoomRepository.save(room);
    }

    //  Save a new message
    public ChatMessage saveMessage(Long chatRoomId, String senderId, String content) {
        Optional<ChatRoom> optionalRoom = chatRoomRepository.findById(chatRoomId);
        if (optionalRoom.isEmpty()) {
            throw new RuntimeException("Chat room not found");
        }

        ChatMessage message = new ChatMessage();
        message.setChatRoom(optionalRoom.get());
        message.setSenderId(senderId);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());

        return (ChatMessage) chatMessageRepository.save(message);
    }

    //  Fetch messages for a room
    public List<ChatMessage> getMessagesForRoom(Long chatRoomId) {
        return chatMessageRepository.findByChatRoomId(chatRoomId);
    }

    //  List all chat rooms (optional)
    public List<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAll();
    }
}
