package com.eyedra.post_service_api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    private String id;
    private String userId;
    private String content;
    private String imageUrl;
    private Date createdAt;
}
