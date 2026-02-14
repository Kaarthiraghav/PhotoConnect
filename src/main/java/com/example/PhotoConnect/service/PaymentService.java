package com.example.PhotoConnect.service;

import com.example.PhotoConnect.model.*;
import com.example.PhotoConnect.repository.*;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final PhotographerProfileRepository photographerProfileRepository;
    private final EmailService emailService;
    private final NotificationService notificationService;

    @Value("${invoice.storage.path:invoices/}")
    private String invoiceStoragePath;

    @Transactional
    public Payment processPayment(Long bookingId, Double amount, String paymentMethod) {
        // Verify booking exists
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Create payment record
        Payment payment = Payment.builder()
                .bookingId(bookingId)
                .amount(amount)
                .paymentMethod(paymentMethod)
                .paymentStatus("COMPLETED")
                .transactionId(generateTransactionId())
                .paidAt(LocalDateTime.now())
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        // Mark booking as paid
        booking.setPaid(true);
        bookingRepository.save(booking);

        // Generate invoice
        try {
            Invoice invoice = generateInvoice(booking, savedPayment);
            
            // Send invoice via email
            User client = userRepository.findById(booking.getClientId()).orElse(null);
            if (client != null && invoice.getPdfPath() != null) {
                emailService.sendEmailWithAttachment(
                    client.getEmail(),
                    "PhotoConnect - Payment Invoice #" + invoice.getInvoiceNumber(),
                    "Dear " + client.getUsername() + ",\n\n" +
                    "Thank you for your payment. Please find your invoice attached.\n\n" +
                    "Invoice Number: " + invoice.getInvoiceNumber() + "\n" +
                    "Amount Paid: $" + invoice.getTotalAmount() + "\n\n" +
                    "Best regards,\nPhotoConnect Team",
                    invoice.getPdfPath()
                );
                invoice.setSentAt(LocalDateTime.now());
                invoiceRepository.save(invoice);
            }

            // Send notification
            notificationService.notifyPaymentReceived(bookingId, booking.getClientId(), 
                client != null ? client.getEmail() : "");

        } catch (Exception e) {
            log.error("Failed to generate or send invoice", e);
        }

        return savedPayment;
    }

    @Transactional
    public Invoice generateInvoice(Booking booking, Payment payment) throws Exception {
        // Get booking details
        User client = userRepository.findById(booking.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));
        
        User photographerUser = userRepository.findById(booking.getPhotographerId())
                .orElseThrow(() -> new RuntimeException("Photographer not found"));

        // Calculate amounts
        Double amount = payment.getAmount();
        Double taxAmount = amount * 0.10; // 10% tax
        Double totalAmount = amount + taxAmount;

        // Generate invoice number
        String invoiceNumber = "INV-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) 
                + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // Create invoice record
        Invoice invoice = Invoice.builder()
                .invoiceNumber(invoiceNumber)
                .bookingId(booking.getId())
                .paymentId(payment.getId())
                .clientName(client.getUsername())
                .clientEmail(client.getEmail())
                .photographerName(photographerUser.getUsername())
                .amount(amount)
                .taxAmount(taxAmount)
                .totalAmount(totalAmount)
                .build();

        Invoice savedInvoice = invoiceRepository.save(invoice);

        // Generate PDF
        try {
            String pdfPath = generateInvoicePDF(savedInvoice, booking, client, photographerUser);
            savedInvoice.setPdfPath(pdfPath);
            invoiceRepository.save(savedInvoice);
        } catch (Exception e) {
            log.error("Failed to generate PDF for invoice {}", invoiceNumber, e);
            throw e;
        }

        return savedInvoice;
    }

    private String generateInvoicePDF(Invoice invoice, Booking booking, User client, User photographer) throws Exception {
        // Ensure directory exists
        File directory = new File(invoiceStoragePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = invoice.getInvoiceNumber() + ".pdf";
        String filePath = invoiceStoragePath + fileName;

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            PdfWriter writer = new PdfWriter(fos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Header
            document.add(new Paragraph("PHOTOCONNECT")
                    .setFontSize(24)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER));
            
            document.add(new Paragraph("INVOICE")
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20));

            // Invoice details
            document.add(new Paragraph("Invoice Number: " + invoice.getInvoiceNumber())
                    .setFontSize(12));
            document.add(new Paragraph("Generated: " + invoice.getGeneratedAt().format(
                    DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm")))
                    .setFontSize(10)
                    .setMarginBottom(20));

            // Client details
            document.add(new Paragraph("Bill To:")
                    .setFontSize(12)
                    .setBold());
            document.add(new Paragraph(client.getUsername())
                    .setFontSize(11));
            document.add(new Paragraph(client.getEmail())
                    .setFontSize(10)
                    .setMarginBottom(15));

            // Photographer details
            document.add(new Paragraph("Photographer:")
                    .setFontSize(12)
                    .setBold());
            document.add(new Paragraph(photographer.getUsername())
                    .setFontSize(11)
                    .setMarginBottom(20));

            // Booking details
            document.add(new Paragraph("Booking Details:")
                    .setFontSize(12)
                    .setBold());
            document.add(new Paragraph("Booking ID: " + booking.getId())
                    .setFontSize(10));
            document.add(new Paragraph("Event Date: " + (booking.getEventDate() != null ? 
                    booking.getEventDate().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")) : "N/A"))
                    .setFontSize(10)
                    .setMarginBottom(20));

            // Payment table
            float[] columnWidths = {3, 1, 1};
            Table table = new Table(columnWidths);
            table.setWidth(500);

            // Table header
            table.addHeaderCell("Description");
            table.addHeaderCell("Quantity");
            table.addHeaderCell("Amount");

            // Table rows
            table.addCell("Photography Service");
            table.addCell("1");
            table.addCell("$" + String.format("%.2f", invoice.getAmount()));

            table.addCell("Tax (10%)");
            table.addCell("");
            table.addCell("$" + String.format("%.2f", invoice.getTaxAmount()));

            table.addCell("Total");
            table.addCell("");
            table.addCell("$" + String.format("%.2f", invoice.getTotalAmount()));

            document.add(table);

            // Footer
            document.add(new Paragraph("\nThank you for your business!")
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(30));

            document.close();
            log.info("Invoice PDF generated at: {}", filePath);
            return filePath;

        } catch (Exception e) {
            log.error("Error generating PDF invoice", e);
            throw e;
        }
    }

    private String generateTransactionId() {
        return "TXN-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public Payment getPaymentByBookingId(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Payment not found for booking"));
    }

    public Invoice getInvoiceByBookingId(Long bookingId) {
        return invoiceRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Invoice not found for booking"));
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }
}
