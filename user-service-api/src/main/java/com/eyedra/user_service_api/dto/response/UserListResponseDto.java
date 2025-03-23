package com.eyedra.user_service_api.dto.response;

import com.eyedra.user_service_api.util.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserListResponseDto {
    private Long userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private Role role;
}
