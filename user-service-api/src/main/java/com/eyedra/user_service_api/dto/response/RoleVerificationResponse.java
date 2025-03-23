package com.eyedra.user_service_api.dto.response;

import com.eyedra.user_service_api.dto.request.UserReqDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleVerificationResponse {
    private List<UserReqDto> users;
}
