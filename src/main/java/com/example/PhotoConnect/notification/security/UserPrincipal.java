package com.example.PhotoConnect.Notification.security;

public class UserPrincipal {

    private Long id;
    private String email;

    public UserPrincipal(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}

