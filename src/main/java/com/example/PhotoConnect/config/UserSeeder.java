package com.example.PhotoConnect.config;

import com.example.PhotoConnect.model.Role;
import com.example.PhotoConnect.model.User;
import com.example.PhotoConnect.model.PhotographerProfile;
import com.example.PhotoConnect.model.ClientProfile;
import com.example.PhotoConnect.repository.RoleRepository;
import com.example.PhotoConnect.repository.UserRepository;
import com.example.PhotoConnect.repository.PhotographerProfileRepository;
import com.example.PhotoConnect.repository.ClientProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class UserSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(UserSeeder.class);

    private static final String ADMIN_EMAIL = "admin@photoconnect.com";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "Admin@123";

    private static final String PHOTOGRAPHER_EMAIL = "photographer@photoconnect.com";
    private static final String PHOTOGRAPHER_USERNAME = "photographer";
    private static final String PHOTOGRAPHER_PASSWORD = "Photo@123";

    private static final String CLIENT_EMAIL = "client@photoconnect.com";
    private static final String CLIENT_USERNAME = "client";
    private static final String CLIENT_PASSWORD = "User@123";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PhotographerProfileRepository photographerProfileRepository;

    @Autowired
    private ClientProfileRepository clientProfileRepository;

    @Override
    public void run(String... args) {
        seedUsers();
    }

    private void seedUsers() {
        try {
            createUserIfNotExists(ADMIN_EMAIL, ADMIN_USERNAME, ADMIN_PASSWORD, "ROLE_ADMIN");
            createUserIfNotExists(PHOTOGRAPHER_EMAIL, PHOTOGRAPHER_USERNAME, PHOTOGRAPHER_PASSWORD, "ROLE_PHOTOGRAPHER");
            createUserIfNotExists(CLIENT_EMAIL, CLIENT_USERNAME, CLIENT_PASSWORD, "ROLE_CLIENT");
            logger.info("User seeding completed successfully");
        } catch (Exception e) {
            logger.error("Error during user seeding: {}", e.getMessage());
        }
    }

    private void createUserIfNotExists(String email, String username, String rawPassword, String roleName) {
        if (userRepository.existsByEmail(email) || userRepository.existsByUsername(username)) {
            logger.info("User already exists: {}", email);
            return;
        }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setVerificationCode(null);
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        user.setRole(role);

        User savedUser = userRepository.save(user);
        logger.info("Created user: {} with role {}", email, roleName);

        // Create appropriate profile based on role
        if (roleName.equals("ROLE_PHOTOGRAPHER")) {
            PhotographerProfile profile = new PhotographerProfile();
            profile.setUserId(savedUser.getId());
            profile.setVerified(true); // Auto-verify seeded photographers
            profile.setStudioName(username + " Studio");
            photographerProfileRepository.save(profile);
            logger.info("Created photographer profile for user: {}", email);
        } else if (roleName.equals("ROLE_CLIENT")) {
            ClientProfile profile = new ClientProfile();
            profile.setUserId(savedUser.getId());
            clientProfileRepository.save(profile);
            logger.info("Created client profile for user: {}", email);
        }
    }
}
