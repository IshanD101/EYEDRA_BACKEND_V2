package com.eyedra.user_service_api.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class UserReqDto {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String mobileNumber;
}
