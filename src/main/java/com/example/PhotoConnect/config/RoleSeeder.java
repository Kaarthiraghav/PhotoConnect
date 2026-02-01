package com.example.PhotoConnect.config;

import com.example.PhotoConnect.model.Role;
import com.example.PhotoConnect.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class RoleSeeder implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(RoleSeeder.class);
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Override
    public void run(String... args) throws Exception {
        seedRoles();
    }
    
    private void seedRoles() {
        try {
            // Check and create Admin role
            if (!roleRepository.existsByName("ROLE_ADMIN")) {
                Role adminRole = new Role();
                adminRole.setName("ROLE_ADMIN");
                roleRepository.save(adminRole);
                logger.info("Created role: ROLE_ADMIN");
            }
            
            // Check and create Photographer role
            if (!roleRepository.existsByName("ROLE_PHOTOGRAPHER")) {
                Role photographerRole = new Role();
                photographerRole.setName("ROLE_PHOTOGRAPHER");
                roleRepository.save(photographerRole);
                logger.info("Created role: ROLE_PHOTOGRAPHER");
            }
            
            // Check and create Client role
            if (!roleRepository.existsByName("ROLE_CLIENT")) {
                Role clientRole = new Role();
                clientRole.setName("ROLE_CLIENT");
                roleRepository.save(clientRole);
                logger.info("Created role: ROLE_CLIENT");
            }
            
            logger.info("Role seeding completed successfully");
            
        } catch (Exception e) {
            logger.error("Error during role seeding: {}", e.getMessage());
        }
    }
}
