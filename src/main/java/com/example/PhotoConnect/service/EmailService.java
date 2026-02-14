package com.example.PhotoConnect.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.url:http://localhost:8080}")
    private String appUrl;

    @Value("${spring.mail.username}")
    private String fromAddress;

    @Async
    public void sendVerificationEmail(String to, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(to);
            message.setSubject("PhotoConnect Account Verification");
            String magicLink = appUrl + "/api/auth/verify-email/" + code;
            message.setText("Welcome to PhotoConnect!\n\n" +
                    "Click the following link to verify your account and sign in automatically:\n" +
                    magicLink + "\n\n" +
                    "If you did not sign up, you can ignore this email.");
            mailSender.send(message);
            log.info("Verification email queued to {} with link {}", to, magicLink);
        } catch (Exception ex) {
            log.error("Failed to send verification email to {}", to, ex);
            throw new RuntimeException("Unable to send verification email. Please try again later.");
        }
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

    @Async
    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            log.info("Email sent to {} with subject: {}", to, subject);
        } catch (Exception ex) {
            log.error("Failed to send email to {}", to, ex);
            throw new RuntimeException("Unable to send email. Please try again later.");
        }
    }

    @Async
    public void sendEmailWithAttachment(String to, String subject, String body, String attachmentPath) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            
            helper.setFrom(fromAddress);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);
            
            if (attachmentPath != null && !attachmentPath.isEmpty()) {
                FileSystemResource file = new FileSystemResource(new File(attachmentPath));
                helper.addAttachment(file.getFilename(), file);
            }
            
            mailSender.send(mimeMessage);
            log.info("Email with attachment sent to {} with subject: {}", to, subject);
        } catch (MessagingException ex) {
            log.error("Failed to send email with attachment to {}", to, ex);
            throw new RuntimeException("Unable to send email with attachment. Please try again later.");
        }
    }
}
