package com.eyedra.notification_service_api.dto;

import lombok.Data;

@Data
public class NotificationRequestDTO {
    private String userId; // optional for broadcast notifications
    private String message;
    private boolean broadcast;
}
