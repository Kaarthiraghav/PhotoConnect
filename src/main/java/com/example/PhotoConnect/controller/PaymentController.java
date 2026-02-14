package com.example.PhotoConnect.controller;

import com.example.PhotoConnect.model.Invoice;
import com.example.PhotoConnect.model.Payment;
import com.example.PhotoConnect.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Process a payment for a booking
     * POST /api/payments/process
     */
    @PostMapping("/process")
    public ResponseEntity<?> processPayment(@RequestBody Map<String, Object> paymentRequest) {
        try {
            Long bookingId = Long.valueOf(paymentRequest.get("bookingId").toString());
            Double amount = Double.valueOf(paymentRequest.get("amount").toString());
            String paymentMethod = paymentRequest.get("paymentMethod").toString();

            Payment payment = paymentService.processPayment(bookingId, amount, paymentMethod);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Payment processed successfully");
            response.put("payment", payment);
            response.put("transactionId", payment.getTransactionId());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Get payment details by booking ID
     * GET /api/payments/booking/{bookingId}
     */
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<?> getPaymentByBookingId(@PathVariable Long bookingId) {
        try {
            Payment payment = paymentService.getPaymentByBookingId(bookingId);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get all payments (admin only)
     * GET /api/payments/all
     */
    @GetMapping("/all")
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    /**
     * Get invoice by booking ID
     * GET /api/payments/invoice/booking/{bookingId}
     */
    @GetMapping("/invoice/booking/{bookingId}")
    public ResponseEntity<?> getInvoiceByBookingId(@PathVariable Long bookingId) {
        try {
            Invoice invoice = paymentService.getInvoiceByBookingId(bookingId);
            return ResponseEntity.ok(invoice);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Download invoice PDF
     * GET /api/payments/invoice/{invoiceId}/download
     */
    @GetMapping("/invoice/{invoiceId}/download")
    public ResponseEntity<Resource> downloadInvoice(@PathVariable Long invoiceId) {
        try {
            Invoice invoice = paymentService.getAllInvoices().stream()
                    .filter(inv -> inv.getId().equals(invoiceId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Invoice not found"));

            if (invoice.getPdfPath() == null || invoice.getPdfPath().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            File file = new File(invoice.getPdfPath());
            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            Resource resource = new FileSystemResource(file);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                            "attachment; filename=\"" + invoice.getInvoiceNumber() + ".pdf\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get all invoices (admin only)
     * GET /api/payments/invoices/all
     */
    @GetMapping("/invoices/all")
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        List<Invoice> invoices = paymentService.getAllInvoices();
        return ResponseEntity.ok(invoices);
    }
}
