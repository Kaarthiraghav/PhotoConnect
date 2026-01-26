package com.example.demo.service;


import com.example.demo.entity.Payment;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;

@Service
public class InvoicePdfService {

    public String generateInvoice(Payment payment) {
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream content = new PDPageContentStream(document, page);
            content.setFont(PDType1Font.HELVETICA_BOLD, 16);
            content.beginText();
            content.newLineAtOffset(50, 750);
            content.showText("INVOICE");
            content.endText();

            content.setFont(PDType1Font.HELVETICA, 12);
            content.beginText();
            content.newLineAtOffset(50, 700);
            content.showText("Booking ID: " + payment.getBookingId());
            content.newLineAtOffset(0, -20);
            content.showText("Amount: " + payment.getAmount());
            content.newLineAtOffset(0, -20);
            content.showText("Paid At: " + LocalDateTime.now());
            content.endText();

            content.close();

            String fileName = "invoice_" + payment.getBookingId() + ".pdf";
            File file = new File("invoices/" + fileName);
            file.getParentFile().mkdirs();
            document.save(file);
            document.close();

            return file.getAbsolutePath();

        } catch (Exception e) {
            throw new RuntimeException("Invoice generation failed");
        }
    }
}

