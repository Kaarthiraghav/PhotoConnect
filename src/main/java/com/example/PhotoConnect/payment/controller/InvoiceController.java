package com.example.demo.controller;

import com.example.demo.entity.Invoice;
import com.example.demo.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceRepository invoiceRepository;

    @GetMapping("/{paymentId}")
    public Invoice getInvoice(@PathVariable Long paymentId) {
        return invoiceRepository.findByPaymentId(paymentId);
    }
}

