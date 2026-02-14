package com.example.PhotoConnect.dto;

import jakarta.validation.constraints.NotBlank;

public class ChatMessageDto {

    @NotBlank(message = "Sender ID cannot be empty")
    private String senderId;

    @NotBlank(message = "Message content cannot be empty")
    private String content;

    // getters & setters
    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
