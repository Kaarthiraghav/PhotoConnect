package com.example.PhotoConnect.search.repository;

import com.example.PhotoConnect.search.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findByLocationContainingIgnoreCaseAndEventTypeContainingIgnoreCaseAndRatingGreaterThanEqual(
            String location,
            String eventType,
            double rating,
            Pageable pageable
    );
}

