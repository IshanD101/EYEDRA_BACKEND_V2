package com.eyedra.community_service_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommunitySpace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String creatorId;

    @ElementCollection
    private Set<Long> membersId = new HashSet<>();
    private LocalDateTime creationDate;
}
