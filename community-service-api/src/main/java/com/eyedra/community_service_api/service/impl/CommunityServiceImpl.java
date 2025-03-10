package com.eyedra.community_service_api.service.impl;

import com.eyedra.community_service_api.dto.request.CommunityReqDto;
import com.eyedra.community_service_api.dto.response.CommunityResponseDto;
import com.eyedra.community_service_api.dto.response.TitleResponse;
import com.eyedra.community_service_api.entity.CommunitySpace;
import com.eyedra.community_service_api.exception.ResourceNotFoundException;
import com.eyedra.community_service_api.repository.CommunitySpaceRepository;
import com.eyedra.community_service_api.service.CommunityService;
import com.eyedra.community_service_api.util.CommunitySpaceMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Getter
@Setter
public class CommunityServiceImpl implements CommunityService {

    @Autowired
    private CommunitySpaceRepository communitySpaceRepository;



    @Override
    public CommunityResponseDto createGroup(CommunityReqDto createGroup) {
        CommunitySpace community = new CommunitySpace();
        community.setTitle(createGroup.getTitle());
        community.setDescription(createGroup.getDescription());
        community.setMembersId(new HashSet<>());
        community.setCreationDate(LocalDateTime.now());
        community.setActive(true);
        communitySpaceRepository.save(community);

        CommunitySpace saved = communitySpaceRepository.save(community);
        return CommunitySpaceMapper.mapToCommunityResponseDto(saved);
    }

    @Override
    public CommunityResponseDto getGroupById(Long communityId) {
        Set<CommunitySpace> communitySpaces = communitySpaceRepository.findByCommunityId(communityId);

        if (communitySpaces.isEmpty()) {
            throw new ResourceNotFoundException("Community not found with id: " + communityId);
        }

        CommunitySpace communitySpace = communitySpaces.iterator().next();
        return CommunitySpaceMapper.mapToCommunityResponseDto(communitySpace);
    }

    @Override
    public CommunityResponseDto updateGroup(Long communityId, CommunityReqDto request) {

        Set<CommunitySpace> community = communitySpaceRepository.findByCommunityId(communityId);

        if (!community.isEmpty()){
            CommunitySpace communitySpace = community.iterator().next();

            communitySpace.setTitle(request.getTitle());
            communitySpace.setDescription(request.getDescription());

            CommunitySpace updatedCommunity = communitySpaceRepository.save(communitySpace);
            return CommunitySpaceMapper.mapToCommunityResponseDto(updatedCommunity);
        }

        return null;
    }

    @Override
    public void deleteGroup(Long communityId) {
        List<CommunitySpace> communities = communitySpaceRepository.findAll();
        Optional<CommunitySpace> communitySpace = communities.stream()
                .filter(c -> c.getCommunityId().equals(communityId))
                .findFirst();

        if (communitySpace.isPresent()) {
            CommunitySpace community = communitySpace.get();
            community.setActive(false);
            communitySpaceRepository.save(community);
        }
    }

    @Override
    public void addMembers(Long communityId, Long membersId) {
        List<CommunitySpace> allCommunities = communitySpaceRepository.findAll();
        Optional<CommunitySpace> communityOpt = allCommunities.stream()
                .filter(c -> c.getCommunityId().equals(communityId))
                .findFirst();

        if (communityOpt.isPresent()) {
            CommunitySpace community = communityOpt.get();
            community.getMembersId().add(membersId);
            communitySpaceRepository.save(community);
        }
    }

    @Override
    public boolean isUserAMemeber(Long membersId, Long communityId) {
        Optional<CommunitySpace> communityUser = communitySpaceRepository
                .findByIdAndMemberId(communityId, membersId);
        return communityUser.isPresent();
    }

    @Override
    public List<TitleResponse> searchByGroupTitle(String title) {
        List<CommunitySpace> communities;

        if (title == null || title.isEmpty()) {
            communities = communitySpaceRepository.findAll();
        } else {
            communities = communitySpaceRepository.findByTitle(title);
        }

        return communities.stream()
                .filter(CommunitySpace::isActive)
                .map(community -> new TitleResponse(community.getCommunityId(), community.getTitle()))
                .collect(Collectors.toList());
    }
}
