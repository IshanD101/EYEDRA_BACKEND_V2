package com.eyedra.comment_service_api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;
import java.time.LocalDateTime; // Import for date and time
import org.springframework.data.annotation.CreatedDate; // Import for created date
import org.springframework.data.annotation.LastModifiedDate; // Import for last modified date

@Document(collection = "comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Comment {
    @CreatedDate
    private LocalDateTime createdAt; // Field for creation timestamp
    @LastModifiedDate
    private LocalDateTime updatedAt; // Field for last update timestamp

    @Id
    private String id;
    private String text;
    private String userId;
    private String postId;
    private boolean rHeart; // Heart reaction
}
