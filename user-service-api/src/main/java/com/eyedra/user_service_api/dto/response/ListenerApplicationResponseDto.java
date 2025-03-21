package com.eyedra.user_service_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListenerApplicationResponseDto {
    private Long applicationId;
    private String username;
    private String description;
    private String certificationImageUrl;
    private String status; // PENDING, APPROVED, REJECTED
    private String reviewComment;
}
