package com.eyedra.community_service_api.service.impl;

import com.eyedra.community_service_api.dto.request.MessageReqDto;
import com.eyedra.community_service_api.dto.response.MessageResponseDto;
import com.eyedra.community_service_api.service.MessageService;

import java.util.List;

public class MessageServiceImpl implements MessageService {
    @Override
    public MessageResponseDto sendMessage(Long communityId, MessageReqDto request) {
        return null;
    }

    @Override
    public List<MessageResponseDto> getGroupMessages(Long communityId) {
        return List.of();
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
