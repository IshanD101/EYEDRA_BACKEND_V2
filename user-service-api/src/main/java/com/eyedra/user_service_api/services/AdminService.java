package com.eyedra.user_service_api.services;

import com.eyedra.user_service_api.dto.response.UserListResponseDto;
import com.eyedra.user_service_api.util.Role;

import java.util.List;

public interface AdminService {
    List<UserListResponseDto> getAllUsers();
    List<UserListResponseDto> getUsersByRole(Role role);
    void updateUserRole(Long userId, Role role);
    void deleteUser(Long userId);
}
