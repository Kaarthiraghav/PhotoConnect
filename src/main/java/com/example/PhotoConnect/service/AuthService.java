package com.example.PhotoConnect.service;

import com.example.PhotoConnect.dto.AuthResponse;
import com.example.PhotoConnect.dto.LoginRequest;
import com.example.PhotoConnect.dto.RegisterRequest;
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

import java.util.Set;
import java.util.Collections;

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
    
    @Transactional
    public AuthResponse registerClient(RegisterRequest registerRequest) {
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
        user.setEnabled(true);
        
        // Assign CLIENT role by default
        Role clientRole = roleRepository.findByName("ROLE_CLIENT")
                .orElseThrow(() -> new RuntimeException("Client role not found. Please ensure roles are seeded."));
        
        user.setRole(clientRole);
        
        // Save user to database
        User savedUser = userRepository.save(user);
        
        // Generate JWT token
        String token = jwtTokenProvider.generateToken(
            new org.springframework.security.core.userdetails.User(
                savedUser.getEmail(),
                savedUser.getPassword(),
                Collections.singletonList(new org.springframework.security.core.authority.SimpleGrantedAuthority(savedUser.getRole().getName()))
            )
        );
        
        // Prepare response
        Set<String> roleNames = Collections.singleton(savedUser.getRole().getName());
        
        return new AuthResponse(token, savedUser.getId(), savedUser.getEmail(), 
                               savedUser.getUsername(), roleNames);
    }
    
                    @Transactional
                    public AuthResponse login(LoginRequest request) {
                    // Authenticate user
                    Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
                    );

                    // Get user details
                    User user = userRepository.findByEmail(request.getEmail())
                        .orElseThrow(() -> new RuntimeException("User not found"));

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
}
