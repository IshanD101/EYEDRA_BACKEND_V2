package com.eyedra.notification_service_api.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationResponseDTO {
    private String id;
    private String userId;
    private String message;
    private boolean read;
    private boolean broadcast;
    private LocalDateTime timestamp;
}
