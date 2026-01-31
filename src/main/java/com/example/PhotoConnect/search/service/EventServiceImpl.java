package com.example.PhotoConnect.search.service;

import com.example.PhotoConnect.search.entity.Event;
import com.example.PhotoConnect.search.repository.EventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Page<Event> searchEvents(String location, String eventType, double rating, Pageable pageable) {

        return eventRepository
                .findByLocationContainingIgnoreCaseAndEventTypeContainingIgnoreCaseAndRatingGreaterThanEqual(
                        location,
                        eventType,
                        rating,
                        pageable
                );
    }
}

