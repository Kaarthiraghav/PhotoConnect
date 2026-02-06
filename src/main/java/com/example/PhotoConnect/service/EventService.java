package com.example.PhotoConnect.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.PhotoConnect.model.Event;

public interface EventService {

    Page<Event> searchEvents(
            String location,
            String eventType,
            double rating,
            Pageable pageable
    );
}
