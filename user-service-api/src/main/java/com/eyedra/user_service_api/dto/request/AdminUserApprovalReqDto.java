package com.eyedra.user_service_api.dto.request;

import com.eyedra.user_service_api.util.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUserApprovalReqDto {
    private Role role;
}
