package com.eyedra.reaction_service_api.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;
@Document(collection = "reactions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Reaction {
    @Id
    private String id;
    private boolean hrt; // e.g., true for like, false for dislike
    private String userId; // ID of the user who made the reaction
    private String postId; // ID of the post the reaction is associated with
}
