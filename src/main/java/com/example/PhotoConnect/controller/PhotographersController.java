package com.example.PhotoConnect.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/photographers")
@CrossOrigin
public class PhotographersController {

    // TODO: Replace hardcoded data with actual database queries
    @GetMapping("/top")
    public List<Map<String, Object>> topPhotographers() {
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(make("John Doe", "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&w=200&q=60", 42));
        list.add(make("Jane Smith", "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&w=200&q=60", 35));
        list.add(make("Alex Johnson", "", 30));
        return list;
    }

    @GetMapping
    public List<Map<String, Object>> allPhotographers() {
        return topPhotographers();
    }

    private Map<String, Object> make(String name, String profilePhoto, int totalEvents) {
        Map<String, Object> m = new HashMap<>();
        m.put("name", name);
        m.put("profilePhoto", profilePhoto);
        m.put("totalEvents", totalEvents);
        return m;
    }
}
