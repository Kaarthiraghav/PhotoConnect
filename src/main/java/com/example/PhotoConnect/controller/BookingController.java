package com.example.PhotoConnect.controller;

import com.example.PhotoConnect.model.Booking;
import com.example.PhotoConnect.service.BookingService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public Booking createBooking(
            @RequestParam Long clientId,
            @RequestParam Long photographerId,
            @RequestParam String eventDate
    ) {
        return bookingService.createBooking(
                clientId,
                photographerId,
                LocalDateTime.parse(eventDate)
        );
    }

    @PutMapping("/{id}/accept")
    public Booking accept(@PathVariable Long id) {
        return bookingService.acceptBooking(id);
    }

    @PutMapping("/{id}/reject")
    public Booking reject(@PathVariable Long id) {
        return bookingService.rejectBooking(id);
    }

    @PutMapping("/{id}/cancel")
    public Booking cancel(@PathVariable Long id) {
        return bookingService.cancelBooking(id);
    }

    @PutMapping("/{id}/pay")
    public Booking pay(@PathVariable Long id) {
        return bookingService.markAsPaid(id);
    }

    @GetMapping
    public List<Booking> getAll() {
        return bookingService.getAllBookings();
    }
}

