package com.example.PhotoConnect.search.repository;

import com.example.PhotoConnect.search.entity.Event;
import org.springframework.data.jpa.domain.Specification;

public class EventSpecification {

    public static Specification<Event> hasLocation(String location) {
        return (root, query, cb) ->
                location == null || location.isEmpty()
                        ? null
                        : cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%");
    }

    public static Specification<Event> hasEventType(String eventType) {
        return (root, query, cb) ->
                eventType == null || eventType.isEmpty()
                        ? null
                        : cb.like(cb.lower(root.get("eventType")), "%" + eventType.toLowerCase() + "%");
    }

    public static Specification<Event> hasRating(double rating) {
        return (root, query, cb) ->
                rating <= 0
                        ? null
                        : cb.greaterThanOrEqualTo(root.get("rating"), rating);
    }
}

