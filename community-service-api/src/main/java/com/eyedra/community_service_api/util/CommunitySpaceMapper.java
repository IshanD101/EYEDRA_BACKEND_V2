package com.eyedra.community_service_api.util;

import com.eyedra.community_service_api.dto.response.CommunityResponseDto;
import com.eyedra.community_service_api.entity.CommunitySpace;

public class CommunitySpaceMapper {

    public static CommunityResponseDto mapToCommunityResponseDto(CommunitySpace community) {
        if (community == null) {
            return null;
        }

        CommunityResponseDto dto = new CommunityResponseDto();
        dto.setCommunityId(community.getCommunityId());
        dto.setTitle(community.getTitle());
        dto.setDescription(community.getDescription());
        dto.setCreatorId(community.getCreatorId());
        dto.setMembersId(community.getMembersId());
        dto.setCreationDate(community.getCreationDate());
        dto.setActive(community.isActive());

        return dto;
    }
}
