package com.example.PhotoConnect.controller;

import com.example.PhotoConnect.dto.UserResponseDTO;
import com.example.PhotoConnect.dto.UserUpdateRequest;
import com.example.PhotoConnect.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    // Get all users with pagination
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<UserResponseDTO> users = adminUserService.getAllUsers(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("users", users.getContent());
        response.put("currentPage", users.getNumber());
        response.put("totalItems", users.getTotalElements());
        response.put("totalPages", users.getTotalPages());

        return ResponseEntity.ok(response);
    }

    // Search users
    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDTO>> searchUsers(
            @RequestParam String keyword) {
        List<UserResponseDTO> users = adminUserService.searchUsers(keyword);
        return ResponseEntity.ok(users);
    }

    // Get single user
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable @org.springframework.lang.NonNull Long id) {
        UserResponseDTO user = adminUserService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateRequest request) {
        UserResponseDTO updatedUser = adminUserService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        adminUserService.deleteUser(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User deleted successfully");
        return ResponseEntity.ok(response);
    }

    // Toggle user status
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<UserResponseDTO> toggleUserStatus(@PathVariable @org.springframework.lang.NonNull Long id) {
        UserResponseDTO user = adminUserService.toggleUserStatus(id);
        return ResponseEntity.ok(user);
    }

    // Get user statistics
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getUserStats() {
        Map<String, Object> stats = new HashMap<>();

        // You'll need to implement these in AdminUserService
        stats.put("totalUsers", 0);
        stats.put("activeUsers", 0);
        stats.put("newUsersToday", 0);
        stats.put("photographers", 0);
        stats.put("clients", 0);

        return ResponseEntity.ok(stats);
    }
}