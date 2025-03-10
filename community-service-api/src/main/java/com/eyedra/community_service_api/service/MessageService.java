package com.eyedra.community_service_api.service;


import com.eyedra.community_service_api.dto.request.MessageReqDto;
import com.eyedra.community_service_api.dto.response.MessageResponseDto;

import java.util.List;

public interface MessageService {
    MessageResponseDto sendMessage(Long communityId, MessageReqDto request);

    List<MessageResponseDto> getGroupMessages(Long communityId);

    void markMessagesAsRead(Long communityId);

    long getUnreadMessagesCount(Long communityId);

    long getTotalUnreadMessagesCount();

    List<MessageResponseDto> getLatestMessages(List<Long> communityIds);
}
