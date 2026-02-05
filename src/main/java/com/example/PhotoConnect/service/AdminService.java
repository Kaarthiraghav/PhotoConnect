package com.example.PhotoConnect.service;

import com.example.PhotoConnect.model.Admin;
import com.example.PhotoConnect.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public Admin createAdmin(String email, String password, String name) {
        Admin admin = new Admin();
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setName(name);
        admin.setRole("ADMIN");

        // Default permissions
        admin.getPermissions().add("VIEW_DASHBOARD");
        admin.getPermissions().add("MANAGE_USERS");
        admin.getPermissions().add("VIEW_REPORTS");

        return adminRepository.save(admin);
    }

    public Admin login(String email, String password) {
        return adminRepository.findByEmail(email)
                .filter(admin -> passwordEncoder.matches(password, admin.getPassword()))
                .orElse(null);
    }

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }
}
