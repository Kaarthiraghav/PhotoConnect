package com.example.PhotoConnect.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_number", unique = true, nullable = false)
    private String invoiceNumber;

    @Column(name = "booking_id", nullable = false)
    private Long bookingId;

    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "client_email")
    private String clientEmail;

    @Column(name = "photographer_name")
    private String photographerName;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "tax_amount")
    private Double taxAmount;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Column(name = "pdf_path")
    private String pdfPath;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        generatedAt = LocalDateTime.now();
    }
}
