package com.example.PhotoConnect.repository;

import com.example.PhotoConnect.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    
    Optional<Invoice> findByBookingId(Long bookingId);
    
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
    
    List<Invoice> findByClientEmail(String clientEmail);
    
    Optional<Invoice> findByPaymentId(Long paymentId);
}
