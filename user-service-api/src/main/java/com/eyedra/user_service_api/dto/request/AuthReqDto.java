package com.eyedra.user_service_api.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthReqDto {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
