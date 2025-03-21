package com.eyedra.notification_service_api.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "notifications")
public class Notification {
    @Id
    private String id;
    private String userId;  // null for broadcast notifications
    private String message;
    private boolean read;
    private boolean broadcast; // new field
    private LocalDateTime timestamp = LocalDateTime.now();
}
