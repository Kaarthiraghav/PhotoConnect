package com.example.PhotoConnect.controller;

import com.example.PhotoConnect.model.PhotographerProfiles;
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
    public PhotographerProfiles save(@RequestBody PhotographerProfiles profile) {
        return service.save(profile);
    }

    @GetMapping("/{userId}")
    public PhotographerProfiles get(@PathVariable Long userId) {
        return service.getByUserId(userId);
    }
}

