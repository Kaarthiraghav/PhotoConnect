package com.example.demo.controller;


import com.example.demo.entity.Payment;
import com.example.demo.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{bookingId}/pay")
    public Payment pay(
            @PathVariable Long bookingId,
            @RequestParam Double amount,
            @RequestParam Long clientId,
            @RequestParam Long photographerId
    ) {
        return paymentService.pay(bookingId, amount, clientId, photographerId);
    }
}
