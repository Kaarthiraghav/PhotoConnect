package com.example.searchservice.controller;


import com.example.searchservice.entity.Event;
import com.example.searchservice.service.EventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/search")
    public Page<Event> searchEvents(
            @RequestParam(defaultValue = "") String location,
            @RequestParam(defaultValue = "") String eventType,
            @RequestParam(defaultValue = "0") double rating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "rating") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        PageRequest pageable = PageRequest.of(page, size, sort);

        return eventService.searchEvents(location, eventType, rating, pageable);
    }
}

