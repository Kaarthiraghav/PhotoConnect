package com.example.searchservice.service;

import com.example.searchservice.entity.Event;
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
