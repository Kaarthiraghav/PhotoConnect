package com.example.PhotoConnect.controller;

import com.example.PhotoConnect.model.PhotographerProfile;
import com.example.PhotoConnect.service.PhotographerProfileService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/photographer/profile")
public class PhotographerProfileController {

    private final PhotographerProfileService service;

    public PhotographerProfileController(PhotographerProfileService service) {
        this.service = service;
    }

    @PostMapping
    public PhotographerProfile save(@RequestBody PhotographerProfile profile) {
        return service.save(profile);
    }

    @GetMapping("/{userId}")
    public PhotographerProfile get(@PathVariable Long userId) {
        return service.getByUserId(userId);
    }
}

