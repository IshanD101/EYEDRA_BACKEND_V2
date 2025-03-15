package com.eyedra.community_service_api.service;


import com.eyedra.community_service_api.dto.request.MessageReqDto;
import com.eyedra.community_service_api.dto.response.MessageResponseDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface MessageService {
    Mono<MessageResponseDto> sendMessage(Long communityId, Long senderId, MessageReqDto request);

    Mono<List<MessageResponseDto>> getGroupMessages(Long communityId, Long userId);

    Mono<Void> markMessagesAsRead(Long communityId, Long userId);

    Mono<Long> getUnreadMessagesCount(Long communityId, Long userId);

    Mono<Long> getTotalUnreadMessagesCount(Long userId);

    Mono<List<MessageResponseDto>> getLatestMessages(List<Long> communityIds, Long userId);
}
