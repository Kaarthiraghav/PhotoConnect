package com.example.PhotoConnect.service;

import com.example.PhotoConnect.model.Booking;
import com.example.PhotoConnect.model.BookingStatus;
import com.example.PhotoConnect.repository.BookingRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    public Booking createBooking(Long clientId, Long photographerId, LocalDateTime eventDate) {
        Booking booking = Booking.builder()
                .clientId(clientId)
                .photographerId(photographerId)
                .eventDate(eventDate)
                .status(BookingStatus.PENDING)
                .paid(false)
                .build();

        return bookingRepository.save(booking);
    }

    public Booking acceptBooking(Long bookingId) {
        Booking booking = getBooking(bookingId);
        booking.setStatus(BookingStatus.ACCEPTED);
        return bookingRepository.save(booking);
    }

    public Booking rejectBooking(Long bookingId) {
        Booking booking = getBooking(bookingId);
        booking.setStatus(BookingStatus.REJECTED);
        return bookingRepository.save(booking);
    }

    public Booking cancelBooking(Long bookingId) {
        Booking booking = getBooking(bookingId);
        booking.setStatus(BookingStatus.CANCELLED);
        return bookingRepository.save(booking);
    }

    public Booking markAsPaid(Long bookingId) {
        Booking booking = getBooking(bookingId);
        booking.setPaid(true);
        booking.setStatus(BookingStatus.PAID);
        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getClientBookings(Long clientId) {
        return bookingRepository.findByClientId(clientId);
    }

    public List<Booking> getPhotographerBookings(Long photographerId) {
        return bookingRepository.findByPhotographerId(photographerId);
    }

    private Booking getBooking(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }
}

