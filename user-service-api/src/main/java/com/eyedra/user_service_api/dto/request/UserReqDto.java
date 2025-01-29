package com.eyedra.user_service_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserReqDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String mobileNumber;
}
