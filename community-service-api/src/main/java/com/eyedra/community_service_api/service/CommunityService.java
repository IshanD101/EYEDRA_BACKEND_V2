package com.eyedra.community_service_api.service;

import com.eyedra.community_service_api.dto.request.CommunityReqDto;
import com.eyedra.community_service_api.dto.response.CommunityResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface CommunityService {
    CommunityResponseDto createGroup(CommunityReqDto createGroup);

    CommunityResponseDto getGroupById(Long communityId);

    CommunityResponseDto updateGroup(Long communityId, CommunityReqDto request);

    void deleteGroup(Long communityId);
}
