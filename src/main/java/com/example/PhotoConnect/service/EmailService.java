package com.example.PhotoConnect.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendVerificationEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("PhotoConnect Account Verification");
        message.setText("Welcome to PhotoConnect!\n\n" +
                "Please verify your account using this code:\n" +
                code + "\n\n" +
                "Or use this link (if frontend supports it): /verify-email?code=" + code);
        mailSender.send(message);
    }

    @Async
    public void sendPasswordResetEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("PhotoConnect Password Reset");
        message.setText("You requested a password reset.\n\n" +
                "Use this token to reset your password:\n" +
                token + "\n\n" +
                "This token expires in 15 minutes.");
        mailSender.send(message);
    }
}
