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
import lombok.NoArgsConstructor;
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

        return null;
    }

    @Override
    public void deleteGroup(Long communityId) {

    }

    @Override
    public void addMembers(Long communityId, Long membersId) {

    }

    @Override
    public boolean isUserAMemeber(Long membersId, Long communityId) {
        return false;
    }

    @Override
    public List<TitleResponse> searchByGroupTitle(String title) {
        return List.of();
    }
}
