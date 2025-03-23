package com.eyedra.user_service_api.util;

import com.eyedra.user_service_api.dto.request.ListenerProfileDto;
import com.eyedra.user_service_api.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ListenerProfileMapper {
    public ListenerProfileDto mapToListenerProfileDto(User user) {
        return ListenerProfileDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .mobileNumber(user.getMobileNumber())
                .description(user.getDescription())
                .imageUrl(user.getImageUrl())
                .build();
    }
}
