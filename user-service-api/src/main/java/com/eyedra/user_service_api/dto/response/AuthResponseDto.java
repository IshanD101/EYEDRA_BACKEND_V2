package com.eyedra.user_service_api.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
public class AuthResponseDto {
    private String token;

    public AuthResponseDto(String token) {
        this.token = token;
    }

}
