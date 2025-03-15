package com.eyedra.user_service_api.controller;

import com.eyedra.user_service_api.dto.request.UserRoleUpdateDto;
import com.eyedra.user_service_api.dto.response.UserListResponseDto;
import com.eyedra.user_service_api.services.AdminService;
import com.eyedra.user_service_api.util.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin")
@CrossOrigin("*")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/aDashboard")
    public String adminDashboard() {
        return "Welcome to the Admin Dashboard!";
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserListResponseDto>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/users/{role}")
    public ResponseEntity<List<UserListResponseDto>> getUsersByRole(@PathVariable Role role) {
        return ResponseEntity.ok(adminService.getUsersByRole(role));
    }

    @PutMapping("/user/{userId}/role")
    public ResponseEntity<String> updateUserRole(@PathVariable Long userId,
                                                 @RequestBody UserRoleUpdateDto roleUpdateDto) {
        adminService.updateUserRole(userId, roleUpdateDto.getRole());
        return ResponseEntity.ok("User role updated successfully");
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }
}
