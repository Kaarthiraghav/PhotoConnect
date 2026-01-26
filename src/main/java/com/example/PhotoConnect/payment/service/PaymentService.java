package com.example.demo.service;



import com.example.demo.entity.*;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoicePdfService invoicePdfService;

    public Payment pay(Long bookingId, Double amount, Long clientId, Long photographerId) {

        paymentRepository.findByBookingId(bookingId)
                .ifPresent(p -> {
                    if (p.getStatus() == PaymentStatus.PAID)
                        throw new RuntimeException("Already paid");
                });

        Payment payment = Payment.builder()
                .bookingId(bookingId)
                .clientId(clientId)
                .photographerId(photographerId)
                .amount(amount)
                .currency("USD")
                .paymentMethod("DUMMY")
                .transactionRef(UUID.randomUUID().toString())
                .status(PaymentStatus.PAID)
                .paidAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);

        String pdfPath = invoicePdfService.generateInvoice(payment);

        Invoice invoice = Invoice.builder()
                .paymentId(payment.getId())
                .invoiceNumber("INV-" + System.currentTimeMillis())
                .pdfUrl(pdfPath)
                .issuedAt(LocalDateTime.now())
                .build();

        invoiceRepository.save(invoice);

        // 🔔 Notify Dev 10 (event or REST later)

        return payment;
    }
}

