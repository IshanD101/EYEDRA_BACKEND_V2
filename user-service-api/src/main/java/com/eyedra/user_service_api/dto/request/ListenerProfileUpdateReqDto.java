package com.eyedra.user_service_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListenerProfileUpdateReqDto {
    private String description;
    private String imageUrl;
}
