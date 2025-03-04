package com.eyedra.community_service_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommunityReqDto {

    @NotBlank(message = "Community name mandatory!!")
    @Size(min = 3, max = 50)
    private String title;

    private String description;

    private Set<Long> membersId;
}
