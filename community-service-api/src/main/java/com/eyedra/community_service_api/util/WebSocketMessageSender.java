package com.eyedra.community_service_api.util;

import com.eyedra.community_service_api.entity.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketMessageSender {

    @Autowired
    private SimpMessagingTemplate messageTemplate;

    public void broadcastMessage(Long communityId, ChatMessage message) {
        messageTemplate.convertAndSend("/topic/community." + communityId,
                ChatMessageMapper.mapToMessageResponseDto(message));
    }

    public void broadcastRead (Long communityId) {
        messageTemplate.convertAndSend("/topic/community/" + communityId
                + "/read","Messages marked as read");
    }
}
