package com.eyedra.community_service_api.service.impl;

import com.eyedra.community_service_api.dto.request.MessageReqDto;
import com.eyedra.community_service_api.dto.response.MessageResponseDto;
import com.eyedra.community_service_api.entity.ChatMessage;
import com.eyedra.community_service_api.repository.MessageSpaceRepository;
import com.eyedra.community_service_api.service.MessageService;
import com.eyedra.community_service_api.util.ChatMessageMapper;
import com.eyedra.community_service_api.util.WebSocketMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageSpaceRepository messageRepository;

    @Autowired
    private WebSocketMessageSender webSocketMessageSender;

    @Override
    public MessageResponseDto sendMessage(Long communityId, MessageReqDto request) {
        ChatMessage message = new ChatMessage();
        message.setContent(request.getContent());
        message.setSenderName(request.getSenderName());

        ChatMessage saved = messageRepository.save(message);

        webSocketMessageSender.broadcastMessage(communityId,saved);

        return ChatMessageMapper.mapToMessageResponseDto(saved);
    }


    @Override
    public List<MessageResponseDto> getGroupMessages(Long messageId) {
        List<ChatMessage> chatMessages = messageRepository.findByMessageId(messageId);

        if (chatMessages == null || chatMessages.isEmpty()) {
            return Collections.emptyList();
        }

        return chatMessages.stream()
                .map(chatMessage -> MessageResponseDto.builder()
                        .messageId(chatMessage.getMessageId())
                        .communityId(chatMessage.getCommunityId())
                        .content(chatMessage.getContent())
                        .senderId(chatMessage.getSenderId())
                        .timestamp(chatMessage.getTimestamp())
                        .build())
                .collect(Collectors.toList());
    }


    @Override
    public void markMessagesAsRead(Long communityId) {

    }

    @Override
    public long getUnreadMessagesCount(Long communityId) {
        return 0;
    }

    @Override
    public long getTotalUnreadMessagesCount() {
        return 0;
    }

    @Override
    public List<MessageResponseDto> getLatestMessages(List<Long> communityIds) {
        return List.of();
    }
}
