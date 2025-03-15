package com.eyedra.user_service_api.services.impl;

import com.eyedra.user_service_api.dto.response.UserListResponseDto;
import com.eyedra.user_service_api.services.AdminService;

import java.util.List;


public class AdminServiceImpl implements AdminService {
    @Override
    public List<UserListResponseDto> getAllUsers() {
        return List.of();
    }

    @Override
    public List<UserListResponseDto> getUsersByRole(String role) {
        return List.of();
    }

    @Override
    public void updateUserRole(Long userId, String role) {

    }

    @Override
    public void deleteUser(Long userId) {

    }
}
