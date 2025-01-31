package com.eyedra.user_service_api.dto.request;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthReqDto {
    private String userName;
    private String password;
}
