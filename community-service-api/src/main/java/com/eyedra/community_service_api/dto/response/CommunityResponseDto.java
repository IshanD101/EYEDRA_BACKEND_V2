package com.eyedra.community_service_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommunityResponseDto {

    private Long communityId;
    private String title;
    private String description;
    private Long creatorId;
    private Set<Long> membersId = new HashSet<>();
    private LocalDateTime creationDate;
    private boolean isActive = true;
    private int memberCount;
    private LocalDateTime lastActivityTime;
}
