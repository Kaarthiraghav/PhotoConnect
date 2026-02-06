package com.example.PhotoConnect.controller;

import com.example.PhotoConnect.model.ClientProfile;
import com.example.PhotoConnect.service.ClientProfileService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client/profile")
public class ClientProfileController {

    private final ClientProfileService service;

    public ClientProfileController(ClientProfileService service) {
        this.service = service;
    }

    @PostMapping
    public ClientProfile save(@RequestBody ClientProfile profile) {
        return service.save(profile);
    }

    @GetMapping("/{userId}")
    public ClientProfile get(@PathVariable Long userId) {
        return service.getByUserId(userId);
    }
}

