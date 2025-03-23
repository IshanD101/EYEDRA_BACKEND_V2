package com.eyedra.user_service_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSummaryResponseDto {
    private Long userId;
    private String username;
    private String firstName;
    private String lastName;
    private String imageUrl;
}
