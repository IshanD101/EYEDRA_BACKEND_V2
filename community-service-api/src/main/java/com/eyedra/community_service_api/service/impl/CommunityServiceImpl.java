package com.eyedra.community_service_api.service.impl;

import com.eyedra.community_service_api.dto.request.CommunityReqDto;
import com.eyedra.community_service_api.dto.response.CommunityResponseDto;
import com.eyedra.community_service_api.dto.response.TitleResponse;
import com.eyedra.community_service_api.entity.CommunitySpace;
import com.eyedra.community_service_api.exception.ResourceNotFoundException;
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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
        Set<CommunitySpace> communitySpaces = communitySpaceRepository.findByCommunityId(communityId);

        if (communitySpaces.isEmpty()) {
            return Mono.error(new ResourceNotFoundException("Community not found with id: " + communityId));
        }

        CommunitySpace communitySpace = communitySpaces.iterator().next();
        return Mono.just(CommunitySpaceMapper.mapToCommunityResponseDto(communitySpace));
    }

    @Override
    public Mono<CommunityResponseDto> updateGroup(String token, Long userId, Long communityId, CommunityReqDto request) {
        return Mono.zip(
                userVerificationService.isListener(token, userId),
                userVerificationService.isAdmin(token, userId)
        ).flatMap(tuple -> {
            boolean isListener = tuple.getT1();
            boolean isAdmin = tuple.getT2();

            Set<CommunitySpace> communitySet = communitySpaceRepository.findByCommunityId(communityId);

            if (communitySet.isEmpty()) {
                return Mono.error(new ResourceNotFoundException("Community not found with id: " + communityId));
            }

            CommunitySpace communitySpace = communitySet.iterator().next();

            boolean isCreator = communitySpace.getCreatorId() != null && communitySpace.getCreatorId().equals(userId);

            if (!isCreator && !isAdmin) {
                return Mono.error(new UnauthorizedException("Only the creator or admin can update the group"));
            }

            communitySpace.setTitle(request.getTitle());
            communitySpace.setDescription(request.getDescription());

            CommunitySpace updatedCommunity = communitySpaceRepository.save(communitySpace);
            return Mono.just(CommunitySpaceMapper.mapToCommunityResponseDto(updatedCommunity));
        });
    }

    @Override
    public Mono<Void> deleteGroup(String token, Long userId, Long communityId) {
        return Mono.zip(
                userVerificationService.isListener(token, userId),
                userVerificationService.isAdmin(token, userId)
        ).flatMap(tuple -> {
            boolean isListener = tuple.getT1();
            boolean isAdmin = tuple.getT2();

            List<CommunitySpace> communities = communitySpaceRepository.findAll();
            Optional<CommunitySpace> communityOpt = communities.stream()
                    .filter(c -> c.getCommunityId().equals(communityId))
                    .findFirst();

            if (communityOpt.isEmpty()) {
                return Mono.error(new ResourceNotFoundException("Community not found with id: " + communityId));
            }

            CommunitySpace community = communityOpt.get();
            boolean isCreator = community.getCreatorId() != null && community.getCreatorId().equals(userId);

            if (!isCreator && !isAdmin) {
                return Mono.error(new UnauthorizedException("Only the creator or admin can delete the group"));
            }

            community.setActive(false);
            communitySpaceRepository.save(community);
            return Mono.empty();
        });
    }

    @Override
    public Mono<Void> addMembers(String token, Long requesterId, Long communityId, Long newMemberId) {
        return Mono.zip(
                userVerificationService.isListener(token, requesterId),
                userVerificationService.isAdmin(token, requesterId)
        ).flatMap(tuple -> {
            boolean isListener = tuple.getT1();
            boolean isAdmin = tuple.getT2();

            List<CommunitySpace> allCommunities = communitySpaceRepository.findAll();
            Optional<CommunitySpace> communityOpt = allCommunities.stream()
                    .filter(c -> c.getCommunityId().equals(communityId))
                    .findFirst();

            if (communityOpt.isEmpty()) {
                return Mono.error(new ResourceNotFoundException("Community not found with id: " + communityId));
            }

            CommunitySpace community = communityOpt.get();
            boolean isCreator = community.getCreatorId() != null && community.getCreatorId().equals(requesterId);

            if (!isCreator && !isListener && !isAdmin) {
                return Mono.error(new UnauthorizedException("Only listeners and admins can add members"));
            }

            // Verify that the new member exists by checking the user service
            return userVerificationService.isUser(token, newMemberId)
                    .flatMap(exists -> {
                        if (!exists) {
                            return Mono.error(new ResourceNotFoundException("User not found with id: " + newMemberId));
                        }
                        community.getMembersId().add(newMemberId);
                        communitySpaceRepository.save(community);
                        return Mono.empty();
                    });
        });
    }

    @Override
    public Mono<Boolean> isUserAMember(Long memberId, Long communityId) {
        Optional<CommunitySpace> communityUser = communitySpaceRepository
                .findByIdAndMemberId(communityId, memberId);
        return Mono.just(communityUser.isPresent());
    }

    @Override
    public Mono<Void> leaveGroup(Long userId, Long communityId) {
        List<CommunitySpace> allCommunities = communitySpaceRepository.findAll();
        Optional<CommunitySpace> communityOpt = allCommunities.stream()
                .filter(c -> c.getCommunityId().equals(communityId))
                .findFirst();

        if (communityOpt.isEmpty()) {
            return Mono.error(new ResourceNotFoundException("Community not found with id: " + communityId));
        }

        CommunitySpace community = communityOpt.get();

        // If user is the creator, they cannot leave unless they transfer ownership first
        if (community.getCreatorId() != null && community.getCreatorId().equals(userId)) {
            return Mono.error(new UnauthorizedException("Creator cannot leave the group. Transfer ownership first."));
        }

        if (community.getMembersId().contains(userId)) {
            community.getMembersId().remove(userId);
            communitySpaceRepository.save(community);
        }

        return Mono.empty();
    }

    @Override
    public Mono<List<TitleResponse>> searchByGroupTitle(String title) {
        List<CommunitySpace> communities;

        if (title == null || title.isEmpty()) {
            communities = communitySpaceRepository.findAll();
        } else {
            communities = communitySpaceRepository.findByTitle(title);
        }

        List<TitleResponse> result = communities.stream()
                .filter(CommunitySpace::isActive)
                .map(community -> new TitleResponse(community.getCommunityId(), community.getTitle()))
                .collect(Collectors.toList());

        return Mono.just(result);
    }
}
