package com.eyedra.community_service_api.service.impl;

import com.eyedra.community_service_api.dto.request.CommunityReqDto;
import com.eyedra.community_service_api.dto.response.CommunityResponseDto;
import com.eyedra.community_service_api.dto.response.TitleResponse;
import com.eyedra.community_service_api.entity.CommunitySpace;
import com.eyedra.community_service_api.exception.UnauthorizedException;
import com.eyedra.community_service_api.repository.CommunitySpaceRepository;
import com.eyedra.community_service_api.service.CommunityService;
import com.eyedra.community_service_api.service.UserVerificationService;
import com.eyedra.community_service_api.util.CommunitySpaceMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
@Getter
@Setter
public class CommunityServiceImpl implements CommunityService {

    @Autowired
    private CommunitySpaceRepository communitySpaceRepository;

    @Autowired
    private UserVerificationService userVerificationService;

    @Override
    public Mono<CommunityResponseDto> createGroup(String token, Long creatorId, CommunityReqDto createGroup) {
        return Mono.zip(
                userVerificationService.isListener(token, creatorId),
                userVerificationService.isAdmin(token, creatorId)
        ).flatMap(tuple -> {
            boolean isListener = tuple.getT1();
            boolean isAdmin = tuple.getT2();

            if (!isListener && !isAdmin) {
                return Mono.error(new UnauthorizedException("Only listeners and admins can create groups"));
            }

            CommunitySpace community = new CommunitySpace();
            community.setTitle(createGroup.getTitle());
            community.setDescription(createGroup.getDescription());
            community.setMembersId(new HashSet<>());
            community.setCreationDate(LocalDateTime.now());
            community.setActive(true);
            community.setCreatorId(creatorId);

            community.getMembersId().add(creatorId);

            CommunitySpace saved = communitySpaceRepository.save(community);
            return Mono.just(CommunitySpaceMapper.mapToCommunityResponseDto(saved));
        });
    }

    @Override
    public Mono<CommunityResponseDto> getGroupById(Long communityId) {
        return null;
    }

    @Override
    public Mono<CommunityResponseDto> updateGroup(String token, Long userId, Long communityId, CommunityReqDto request) {
        return null;
    }

    @Override
    public Mono<Void> deleteGroup(String token, Long userId, Long communityId) {
        return null;
    }

    @Override
    public Mono<Void> addMembers(String token, Long requesterId, Long communityId, Long newMemberId) {
        return null;
    }

    @Override
    public Mono<Boolean> isUserAMember(Long memberId, Long communityId) {
        return null;
    }

    @Override
    public Mono<Void> leaveGroup(Long userId, Long communityId) {
        return null;
    }

    @Override
    public Mono<List<TitleResponse>> searchByGroupTitle(String title) {
        return null;
    }
}
