package com.example.PhotoConnect.service;

import com.example.PhotoConnect.dto.AuthResponse;
import com.example.PhotoConnect.dto.LoginRequest;
import com.example.PhotoConnect.dto.RegisterRequest;
import com.example.PhotoConnect.dto.VerifyEmailRequest;
import com.example.PhotoConnect.dto.ForgotPasswordRequest;
import com.example.PhotoConnect.dto.ResetPasswordRequest;
import com.example.PhotoConnect.model.Role;
import com.example.PhotoConnect.model.User;
import com.example.PhotoConnect.repository.RoleRepository;
import com.example.PhotoConnect.repository.UserRepository;
import com.example.PhotoConnect.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmailService emailService;

    @Transactional
    public String registerClient(RegisterRequest registerRequest) {
        // Check if email already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email is already registered");
        }

        // Check if username already exists
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }

        // Create new user
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEnabled(false); // Disable until verified

        String verificationCode = UUID.randomUUID().toString();
        user.setVerificationCode(verificationCode);

        // Assign CLIENT role by default
        Role clientRole = roleRepository.findByName("ROLE_CLIENT")
                .orElseThrow(() -> new RuntimeException("Client role not found. Please ensure roles are seeded."));

        user.setRole(clientRole);

        // Save user to database
        userRepository.save(user);

        // Send verification email
        emailService.sendVerificationEmail(user.getEmail(), verificationCode);

        return "User registered successfully. Please check your email to verify your account.";
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        // Get user details
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Account not verified. Please check your email.");
        }

        // Generate JWT token
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtTokenProvider.generateToken(userDetails);

        // Build response
        Set<String> roleNames = Collections.singleton(user.getRole().getName());

        return new AuthResponse(token, user.getId(), user.getEmail(),
                user.getUsername(), roleNames);
    }

    public void logout() {
        // Stateless JWT; clients should delete token locally
    }

    @Transactional
    public void verifyEmail(VerifyEmailRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isEnabled()) {
            throw new RuntimeException("User is already verified");
        }

        if (request.getVerificationCode().equals(user.getVerificationCode())) {
            user.setEnabled(true);
            user.setVerificationCode(null);
            userRepository.save(user);
        } else {
            throw new RuntimeException("Invalid verification code");
        }
    }

    @Transactional
    public String verifyEmailByCodeAndLogin(String code) {
        User user = userRepository.findByVerificationCode(code)
                .orElseThrow(() -> new RuntimeException("Invalid or expired verification link"));

        if (!user.isEnabled()) {
            user.setEnabled(true);
        }
        user.setVerificationCode(null);
        userRepository.save(user);

        return jwtTokenProvider.generateTokenForEmail(user.getEmail());
    }

    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);

        emailService.sendPasswordResetEmail(user.getEmail(), token);
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByResetToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid reset token"));

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Reset token has expired");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
    }

    @Transactional
    public String resendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.isEnabled()) {
            return "User already verified";
        }
        String code = user.getVerificationCode();
        if (code == null || code.isBlank()) {
            code = UUID.randomUUID().toString();
            user.setVerificationCode(code);
            userRepository.save(user);
        }
        emailService.sendVerificationEmail(user.getEmail(), code);
        return "Verification email sent";
    }
}
