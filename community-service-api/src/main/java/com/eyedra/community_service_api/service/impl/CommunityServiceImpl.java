package com.eyedra.community_service_api.service.impl;

import com.eyedra.community_service_api.dto.request.CommunityReqDto;
import com.eyedra.community_service_api.dto.response.CommunityResponseDto;
import com.eyedra.community_service_api.dto.response.TitleResponse;
import com.eyedra.community_service_api.service.CommunityService;

import java.util.List;

public class CommunityServiceImpl implements CommunityService {

    @Override
    public CommunityResponseDto createGroup(CommunityReqDto createGroup) {
        return null;
    }

    @Override
    public CommunityResponseDto getGroupById(Long communityId) {

        return null;
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
