package com.eyedra.community_service_api.service;

import com.eyedra.community_service_api.dto.request.CommunityReqDto;
import com.eyedra.community_service_api.dto.response.CommunityResponseDto;
import com.eyedra.community_service_api.dto.response.TitleResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CommunityService {

    Mono<CommunityResponseDto> createGroup(String token, Long creatorId, CommunityReqDto createGroup);

    Mono<CommunityResponseDto> getGroupById(Long communityId);

    Mono<CommunityResponseDto> updateGroup(String token, Long userId, Long communityId, CommunityReqDto request);

    Mono<Void> deleteGroup(String token, Long userId, Long communityId);

    Mono<Void> addMembers(String token, Long requesterId, Long communityId, Long newMemberId);

    Mono<Boolean> isUserAMember(Long memberId, Long communityId);

    Mono<Void> leaveGroup(Long userId, Long communityId);

    Mono<List<TitleResponse>> searchByGroupTitle(String title);
}