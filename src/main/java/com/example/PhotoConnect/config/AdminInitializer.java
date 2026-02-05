package com.example.PhotoConnect.config;

import com.example.PhotoConnect.model.Admin;
import com.example.PhotoConnect.repository.AdminRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        // Check if admin already exists
        if (adminRepository.count() == 0) {
            Admin admin = new Admin();
            admin.setEmail("admin@photoconnect.com");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setName("System Administrator");
            admin.setRole("SUPER_ADMIN");

            // Add all permissions
            admin.getPermissions().add("VIEW_DASHBOARD");
            admin.getPermissions().add("MANAGE_USERS");
            admin.getPermissions().add("MANAGE_PHOTOGRAPHERS");
            admin.getPermissions().add("MANAGE_BOOKINGS");
            admin.getPermissions().add("VIEW_REPORTS");
            admin.getPermissions().add("SYSTEM_SETTINGS");

            adminRepository.save(admin);

            System.out.println("======================================");
            System.out.println("✅ ADMIN USER CREATED SUCCESSFULLY!");
            System.out.println("📧 Email: admin@photoconnect.com");
            System.out.println("🔑 Password: Admin@123");
            System.out.println("======================================");
        } else {
            System.out.println("ℹ️  Admin user already exists");
        }
    }
}