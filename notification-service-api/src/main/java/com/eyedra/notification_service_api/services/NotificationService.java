package com.eyedra.notification_service_api.services;

import com.eyedra.notification_service_api.dto.NotificationRequestDTO;
import com.eyedra.notification_service_api.dto.NotificationResponseDTO;
import com.eyedra.notification_service_api.dto.NotificationUpdateDTO;
import com.eyedra.notification_service_api.entity.Notification;
import com.eyedra.notification_service_api.repository.NotificationRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Mono<NotificationResponseDTO> createNotification(NotificationRequestDTO requestDTO) {
        Notification notification = new Notification();
        notification.setUserId(requestDTO.getUserId());
        notification.setMessage(requestDTO.getMessage());
        notification.setBroadcast(requestDTO.isBroadcast());
        notification.setRead(false);

        return notificationRepository.save(notification)
                .map(this::mapToResponseDTO);
    }

    public Flux<NotificationResponseDTO> getAllNotifications() {
        return notificationRepository.findAll()
                .map(this::mapToResponseDTO);
    }

    public Flux<NotificationResponseDTO> getNotificationsByUserId(String userId) {
        return notificationRepository.findByUserIdOrBroadcastTrue(userId)
                .map(this::mapToResponseDTO);
    }

    public Flux<NotificationResponseDTO> getBroadcastNotifications() {
        return notificationRepository.findByBroadcastTrue()
                .map(this::mapToResponseDTO);
    }

    public Mono<NotificationResponseDTO> getNotificationById(String id) {
        return notificationRepository.findById(id)
                .map(this::mapToResponseDTO);
    }

    public Mono<NotificationResponseDTO> updateNotification(String id, NotificationUpdateDTO updateDTO) {
        return notificationRepository.findById(id)
                .flatMap(notification -> {
                    if (updateDTO.getMessage() != null) {
                        notification.setMessage(updateDTO.getMessage());
                    }
                    if (updateDTO.getRead() != null) {
                        notification.setRead(updateDTO.getRead());
                    }
                    return notificationRepository.save(notification);
                })
                .map(this::mapToResponseDTO);
    }

    public Mono<Void> deleteNotification(String id) {
        return notificationRepository.deleteById(id);
    }

    private NotificationResponseDTO mapToResponseDTO(Notification notification) {
        NotificationResponseDTO responseDTO = new NotificationResponseDTO();
        BeanUtils.copyProperties(notification, responseDTO);
        return responseDTO;
    }
}
