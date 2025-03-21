package com.eyedra.user_service_api.util;

import com.eyedra.user_service_api.dto.response.UserSummaryResponseDto;
import com.eyedra.user_service_api.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserSummaryMapper {
    public UserSummaryResponseDto mapToUserSummaryDto(User user) {
        return UserSummaryResponseDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .imageUrl(user.getImageUrl())
                .build();
    }
}
