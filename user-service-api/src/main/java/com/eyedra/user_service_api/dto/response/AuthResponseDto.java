package com.eyedra.user_service_api.dto.response;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
public class AuthResponseDto {
    private String accessToken;
}
