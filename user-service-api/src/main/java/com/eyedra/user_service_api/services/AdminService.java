package com.eyedra.user_service_api.services;

import com.eyedra.user_service_api.dto.response.UserListResponseDto;

import java.util.List;

public interface AdminService {
    List<UserListResponseDto> getAllUsers();
    List<UserListResponseDto> getUsersByRole(String role);
    void updateUserRole(Long userId, String role);
    void deleteUser(Long userId);
}
