package com.example.PhotoConnect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    
    private String token;
    private String type = "Bearer";
    private Long id;
    private String email;
    private String username;
    private Set<String> roles;

    public AuthResponse(String token, Long id, String email, String username, Set<String> roles) {
        this.token = token;
        this.type = "Bearer";
        this.id = id;
        this.email = email;
        this.username = username;
        this.roles = roles;
    }
}
