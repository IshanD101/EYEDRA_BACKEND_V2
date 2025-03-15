package com.eyedra.user_service_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListenerProfileDto {
    private Long userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private String description;
    private String imageUrl;
}
