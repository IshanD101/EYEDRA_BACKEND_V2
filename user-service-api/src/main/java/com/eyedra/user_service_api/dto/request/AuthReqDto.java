package com.eyedra.user_service_api.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthReqDto {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String mobileNumber;

}
