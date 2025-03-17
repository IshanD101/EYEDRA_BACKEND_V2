package com.eyedra.notification_service_api.dto;

import lombok.Data;

@Data
public class NotificationUpdateDTO {
    private String message;
    private Boolean read;
}
