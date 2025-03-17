package com.eyedra.user_service_api.util;

import com.eyedra.user_service_api.dto.response.UserListResponseDto;
import com.eyedra.user_service_api.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserListResponseDto mapToUserListDto(User user) {
        return UserListResponseDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .mobileNumber(user.getMobileNumber())
                .role(user.getRole())
                .build();
    }
}
