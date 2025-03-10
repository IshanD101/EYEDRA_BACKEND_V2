package com.eyedra.community_service_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TitleResponse {
    private Long communityId;
    private String title;

}
