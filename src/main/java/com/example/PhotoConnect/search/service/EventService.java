package com.example.PhotoConnect.search.service;

import com.example.PhotoConnect.search.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventService {

    Page<Event> searchEvents(
            String location,
            String eventType,
            double rating,
            Pageable pageable
    );
}
