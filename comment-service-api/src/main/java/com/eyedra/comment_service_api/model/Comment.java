package com.eyedra.comment_service_api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;

@Document(collection = "comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Comment {
    @Id
    private String id;
    private String text;
    private String userId;
    private String postId;
    private boolean rHeart; // Heart reaction
}
