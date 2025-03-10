package com.eyedra.community_service_api.service;

import com.eyedra.community_service_api.dto.request.CommunityReqDto;
import com.eyedra.community_service_api.dto.response.CommunityResponseDto;
import com.eyedra.community_service_api.dto.response.TitleResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CommunityService {
    CommunityResponseDto createGroup(CommunityReqDto createGroup);

    CommunityResponseDto getGroupById(Long communityId);

    CommunityResponseDto updateGroup(Long communityId, CommunityReqDto request);

    void deleteGroup(Long communityId);

    void addMembers(Long communityId, Long membersId);

    boolean isUserAMemeber(Long membersId, Long communityId);

    List<TitleResponse> searchByGroupTitle(String title);
}
