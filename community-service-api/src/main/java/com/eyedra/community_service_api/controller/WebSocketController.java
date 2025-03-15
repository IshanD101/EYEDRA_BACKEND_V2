package com.eyedra.community_service_api.controller;

import com.eyedra.community_service_api.dto.request.MessageReqDto;
import com.eyedra.community_service_api.service.CommunityService;
import com.eyedra.community_service_api.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
public class WebSocketController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageService messageService;

    @Autowired
    private CommunityService communityService;

    @MessageMapping("/chat.sendMessage/{communityId}/{senderId}")
    public void sendMessage(
            @DestinationVariable Long communityId,
            @DestinationVariable Long senderId,
            @Payload MessageReqDto messageRequest) {

        communityService.isUserAMember(senderId, communityId)
                .flatMap(isMember -> {
                    if (isMember) {
                        return messageService.sendMessage(communityId, senderId, messageRequest);
                    }
                    return Mono.empty();
                })
                .subscribe(message -> {
                    System.out.println("WebSocket message processed for community: " + communityId);
                });
    }

    @MessageMapping("/chat.joinCommunity/{communityId}/{userId}")
    public void joinCommunity(
            @DestinationVariable Long communityId,
            @DestinationVariable Long userId) {

        communityService.isUserAMember(userId, communityId)
                .subscribe(isMember -> {
                    if (isMember) {
                        // Send a system message that user has joined
                        MessageReqDto systemMessage = new MessageReqDto();
                        systemMessage.setContent("User " + userId + " has joined the chat");
                        systemMessage.setSenderName("System");

                        messagingTemplate.convertAndSend(
                                "/topic/community." + communityId,
                                systemMessage
                        );
                    }
                });
    }
}
