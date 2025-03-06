package com.eyedra.community_service_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDto {
    private Long messageId;
    private Long communityId;
    private Long senderId;
    private String senderName;
    private String content;
    private LocalDateTime timestamp;
    private boolean isDeleted = false;
}
