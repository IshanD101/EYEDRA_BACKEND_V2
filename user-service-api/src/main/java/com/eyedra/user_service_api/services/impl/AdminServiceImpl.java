package com.eyedra.user_service_api.services.impl;

import com.eyedra.user_service_api.dto.response.UserListResponseDto;
import com.eyedra.user_service_api.entity.User;
import com.eyedra.user_service_api.repository.UserRepository;
import com.eyedra.user_service_api.services.AdminService;
import com.eyedra.user_service_api.util.Role;
import com.eyedra.user_service_api.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public List<UserListResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::mapToUserListDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserListResponseDto> getUsersByRole(Role role) {

        return userRepository.findByRole(role).stream()
                .map(userMapper::mapToUserListDto)
                .collect(Collectors.toList());
    }

    @Override
    public void updateUserRole(Long userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with ID: " + userId);
        }
        userRepository.deleteById(userId);
    }

}
