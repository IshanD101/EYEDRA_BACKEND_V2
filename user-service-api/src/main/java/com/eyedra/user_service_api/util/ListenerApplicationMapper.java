package com.eyedra.user_service_api.util;

import com.eyedra.user_service_api.dto.response.ListenerApplicationResponseDto;
import com.eyedra.user_service_api.entity.ListenerApplication;
import org.springframework.stereotype.Component;

@Component
public class ListenerApplicationMapper {

    public ListenerApplicationResponseDto mapToListenerApplicationResponse(ListenerApplication application) {
        return ListenerApplicationResponseDto.builder()
                .id(application.getId())
                .username(application.getUser().getUsername())
                .description(application.getDescription())
                .certificationImageUrl(application.getCertificationImageUrl())
                .status(application.getStatus().name())
                .reviewComment(application.getReviewComment())
                .createdAt(application.getCreatedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
    }
}
