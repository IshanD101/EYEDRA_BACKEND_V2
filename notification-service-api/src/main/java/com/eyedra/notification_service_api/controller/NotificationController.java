package com.eyedra.notification_service_api.controller;

import com.eyedra.notification_service_api.dto.NotificationRequestDTO;
import com.eyedra.notification_service_api.dto.NotificationResponseDTO;
import com.eyedra.notification_service_api.dto.NotificationUpdateDTO;
import com.eyedra.notification_service_api.services.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<NotificationResponseDTO> createNotification(@RequestBody NotificationRequestDTO requestDTO) {
        return notificationService.createNotification(requestDTO);
    }

    @GetMapping
    public Flux<NotificationResponseDTO> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    @GetMapping("/user/{userId}")
    public Flux<NotificationResponseDTO> getNotificationsByUserId(@PathVariable String userId) {
        return notificationService.getNotificationsByUserId(userId);
    }

    @GetMapping("/{id}")
    public Mono<NotificationResponseDTO> getNotificationById(@PathVariable String id) {
        return notificationService.getNotificationById(id);
    }

    @GetMapping("/broadcast")
    public Flux<NotificationResponseDTO> getBroadcastNotifications() {
        return notificationService.getBroadcastNotifications();
    }

    @PutMapping("/{id}")
    public Mono<NotificationResponseDTO> updateNotification(
            @PathVariable String id,
            @RequestBody NotificationUpdateDTO updateDTO) {
        return notificationService.updateNotification(id, updateDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteNotification(@PathVariable String id) {
        return notificationService.deleteNotification(id);
    }
}
