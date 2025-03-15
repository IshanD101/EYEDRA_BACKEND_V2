package com.eyedra.community_service_api.util;

import com.eyedra.community_service_api.dto.response.MessageResponseDto;
import com.eyedra.community_service_api.entity.ChatMessage;

import java.util.ArrayList;

public class ChatMessageMapper {

    public static MessageResponseDto mapToMessageResponseDto(ChatMessage chatMessage) {
        if (chatMessage == null) {
            return null;
        }

        boolean isRead = chatMessage.getReadByUserIds() != null && !chatMessage.getReadByUserIds().isEmpty();

        return MessageResponseDto.builder()
                .messageId(chatMessage.getMessageId())
                .communityId(chatMessage.getCommunityId())
                .senderId(chatMessage.getSenderId())
                .senderName(chatMessage.getSenderName())
                .content(chatMessage.getContent())
                .timestamp(chatMessage.getTimestamp())
                .isDeleted(false)
                .isRead(isRead)
                .build();
    }
}
