package com.eyedra.reaction_service_api.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateReactionDTO {
        private String id;
        private boolean hrt;
        private String userId;
        private String postId;
    }