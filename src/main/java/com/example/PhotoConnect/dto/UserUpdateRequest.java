package com.example.PhotoConnect.dto;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String name;
    private String phone;
    private boolean enabled;
}
