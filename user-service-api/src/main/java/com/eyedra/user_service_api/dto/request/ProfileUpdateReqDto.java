package com.eyedra.user_service_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileUpdateReqDto {
    private String username;
    private String description;
    private String imageUrl;
}
