package com.eyedra.user_service_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListenerApplicationReviewReqDto {
    private Long applicationId;
    private String status; // "APPROVED" or "REJECTED"
    private String reviewComment;
}
