package com.eyedra.community_service_api.entity;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "group_community")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommunitySpace {

    @Id
    private String id;

    private Long communityId;
    private String title;
    private String description;
    private Long creatorId;
    private Set<Long> membersId = new HashSet<>();
    private LocalDateTime creationDate;
    private boolean isActive = true;
}
