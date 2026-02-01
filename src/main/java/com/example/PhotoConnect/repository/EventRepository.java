package com.example.PhotoConnect.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.PhotoConnect.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findByLocationContainingIgnoreCaseAndEventTypeContainingIgnoreCaseAndRatingGreaterThanEqual(
            String location,
            String eventType,
            double rating,
            Pageable pageable
    );
}

