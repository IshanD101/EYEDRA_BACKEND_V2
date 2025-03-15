package com.eyedra.community_service_api.service.impl;

import com.eyedra.community_service_api.dto.request.MessageReqDto;
import com.eyedra.community_service_api.dto.response.MessageResponseDto;
import com.eyedra.community_service_api.entity.ChatMessage;
import com.eyedra.community_service_api.exception.UnauthorizedException;
import com.eyedra.community_service_api.repository.MessageSpaceRepository;
import com.eyedra.community_service_api.service.CommunityService;
import com.eyedra.community_service_api.service.MessageService;
import com.eyedra.community_service_api.util.ChatMessageMapper;
import com.eyedra.community_service_api.util.WebSocketMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageSpaceRepository messageRepository;

    @Autowired
    private WebSocketMessageSender webSocketMessageSender;

    @Autowired
    private CommunityService communityService;

    @Override
    public Mono<MessageResponseDto> sendMessage(Long communityId, Long senderId, MessageReqDto request) {
        return communityService.isUserAMember(senderId, communityId)
                .flatMap(isMember -> {
                    if (!isMember) {
                        return Mono.error(new UnauthorizedException("User is not a member of this community"));
                    }

                    ChatMessage message = new ChatMessage();
                    message.setContent(request.getContent());
                    message.setSenderName(request.getSenderName());
                    message.setSenderId(senderId);
                    message.setCommunityId(communityId);
                    message.setTimestamp(LocalDateTime.now());

                    List<Long> readByUserIds = new ArrayList<>();
                    readByUserIds.add(senderId);
                    message.setReadByUserIds(readByUserIds);

                    ChatMessage saved = messageRepository.save(message);

                    webSocketMessageSender.broadcastMessage(communityId, saved);

                    return Mono.just(ChatMessageMapper.mapToMessageResponseDto(saved));
                });
    }

    @Override
    public Mono<List<MessageResponseDto>> getGroupMessages(Long communityId, Long userId) {
        return communityService.isUserAMember(userId, communityId)
                .flatMap(isMember -> {
                    if (!isMember) {
                        return Mono.error(new UnauthorizedException("User is not a member of this community"));
                    }

                    List<ChatMessage> chatMessages = messageRepository.findByCommunityId(communityId);

                    if (chatMessages == null || chatMessages.isEmpty()) {
                        return Mono.just(Collections.emptyList());
                    }

                    List<MessageResponseDto> messages = chatMessages.stream()
                            .map(ChatMessageMapper::mapToMessageResponseDto)
                            .collect(Collectors.toList());

                    return Mono.just(messages);
                });
    }

    @Override
    public Mono<Void> markMessagesAsRead(Long communityId, Long userId) {
        return communityService.isUserAMember(userId, communityId)
                .flatMap(isMember -> {
                    if (!isMember) {
                        return Mono.error(new UnauthorizedException("User is not a member of this community"));
                    }

                    List<ChatMessage> unreadMessages = messageRepository.findUnreadMessagesByCommunityAndUser(
                            communityId, userId);

                    if (unreadMessages != null && !unreadMessages.isEmpty()) {
                        for (ChatMessage message : unreadMessages) {
                            if (message.getReadByUserIds() == null) {
                                message.setReadByUserIds(new ArrayList<>());
                            }
                            if (!message.getReadByUserIds().contains(userId)) {
                                message.getReadByUserIds().add(userId);
                            }
                        }
                        messageRepository.saveAll(unreadMessages);
                    }

                    return Mono.empty();
                });
    }

    @Override
    public Mono<Long> getUnreadMessagesCount(Long communityId, Long userId) {
        return communityService.isUserAMember(userId, communityId)
                .flatMap(isMember -> {
                    if (!isMember) {
                        return Mono.just(0L);
                    }

                    long count = messageRepository.countUnreadMessagesByCommunityAndUser(communityId, userId);
                    return Mono.just(count);
                });
    }

    @Override
    public Mono<Long> getTotalUnreadMessagesCount(Long userId) {
        long count = messageRepository.countAllUnreadMessagesByUser(userId);
        return Mono.just(count);
    }

    @Override
    public Mono<List<MessageResponseDto>> getLatestMessages(List<Long> communityIds, Long userId) {
        List<ChatMessage> latestMessages = messageRepository.findLatestMessagesByUserAndCommunities(userId, communityIds);

        List<MessageResponseDto> messages = latestMessages.stream()
                .map(ChatMessageMapper::mapToMessageResponseDto)
                .collect(Collectors.toList());

        return Mono.just(messages);
    }
}
