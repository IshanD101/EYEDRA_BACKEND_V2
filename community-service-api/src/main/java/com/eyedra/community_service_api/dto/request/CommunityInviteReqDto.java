package com.eyedra.community_service_api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommunityInviteReqDto {
    @NotNull(message = "Community ID is required")
    private Long communityId;

    @NotNull(message = "At least one user ID must be provided")
    private List<Long> userIds;

    private String inviteMessage;
}
