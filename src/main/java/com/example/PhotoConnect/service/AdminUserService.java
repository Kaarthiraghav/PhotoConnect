package com.example.PhotoConnect.service;

import com.example.PhotoConnect.dto.UserResponseDTO;
import com.example.PhotoConnect.dto.UserUpdateRequest;
import com.example.PhotoConnect.model.User;
import com.example.PhotoConnect.repository.UserRepository;
import com.example.PhotoConnect.repository.PhotographerProfileRepository;
import com.example.PhotoConnect.repository.ClientProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final PhotographerProfileRepository photographerProfileRepository;
    private final ClientProfileRepository clientProfileRepository;

    // Get all users with pagination
    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        if (pageable == null) {
            throw new IllegalArgumentException("Pageable must not be null");
        }
        return userRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    // Search users
    public List<UserResponseDTO> searchUsers(String keyword) {
        // Simple search by username or email
        List<User> users = userRepository.findAll()
                .stream()
                .filter(user -> (user.getUsername() != null
                        && user.getUsername().toLowerCase().contains(keyword.toLowerCase())) ||
                        (user.getEmail() != null && user.getEmail().toLowerCase().contains(keyword.toLowerCase())))
                .collect(Collectors.toList());

        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get user by ID
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDTO(user);
    }

    // Update user
    public UserResponseDTO updateUser(Long id, UserUpdateRequest request) {
        if (id == null) {
            throw new IllegalArgumentException("User id must not be null");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getName() != null) {
            user.setUsername(request.getName());
        }
        if (request.getPhone() != null) {
            // Update client profile phone if exists
            clientProfileRepository.findByUserId(user.getId()).ifPresent(cp -> {
                cp.setPhone(request.getPhone());
                clientProfileRepository.save(cp);
            });
        }

        user.setEnabled(request.isEnabled());

        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    // Delete user (soft delete - just disable)
    public void deleteUser(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("User id must not be null");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEnabled(false);
        userRepository.save(user);
    }

    // Toggle user status
    public UserResponseDTO toggleUserStatus(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEnabled(!user.isEnabled());
        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    // Convert User entity to DTO
    private UserResponseDTO convertToDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getUsername());
        dto.setEmail(user.getEmail());
        // Try to get phone from client profile
        clientProfileRepository.findByUserId(user.getId()).ifPresent(cp -> dto.setPhone(cp.getPhone()));
        dto.setEnabled(user.isEnabled());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());

        // Get roles
        if (user.getRole() != null) {
            dto.setRoles(new HashSet<>() {
                {
                    add(user.getRole().getName());
                }
            });
        } else {
            dto.setRoles(new HashSet<>());
        }

        // Check if photographer
        photographerProfileRepository.findByUserId(user.getId()).ifPresent(pp -> {
            dto.setPhotographer(true);
            dto.setPhotographerVerified(pp.isVerified());
            dto.setPhotographerSpecialization(pp.getStudioName());
        });

        // Check if client
        clientProfileRepository.findByUserId(user.getId()).ifPresent(cp -> dto.setClient(true));

        return dto;
    }
}