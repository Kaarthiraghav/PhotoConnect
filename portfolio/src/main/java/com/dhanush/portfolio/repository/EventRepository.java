package com.dhanush.portfolio.repository;

import com.dhanush.portfolio.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByEventDateBetween(LocalDate startDate, LocalDate endDate);
    List<Event> findByClientName(String clientName);
    List<Event> findByEventType(String eventType);
}