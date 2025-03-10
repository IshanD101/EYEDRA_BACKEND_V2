package com.eyedra.community_service_api.util;

import com.eyedra.community_service_api.dto.response.MessageResponseDto;
import com.eyedra.community_service_api.entity.ChatMessage;

public class ChatMessageMapper {

    public static MessageResponseDto mapToMessageResponseDto(ChatMessage chatMessage) {
        if (chatMessage == null) {
            return null;
        }

        MessageResponseDto messageResponseDto = new MessageResponseDto();
        messageResponseDto.setMessageId(chatMessage.getMessageId());
        messageResponseDto.setCommunityId(chatMessage.getCommunityId());
        messageResponseDto.setSenderId(chatMessage.getSenderId());
        messageResponseDto.setSenderName(chatMessage.getSenderName());
        messageResponseDto.setContent(chatMessage.getContent());
        messageResponseDto.setTimestamp(chatMessage.getTimestamp());
        messageResponseDto.setDeleted(chatMessage.isDeleted());

        return messageResponseDto;

    }
}
