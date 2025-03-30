package com.eyedra.post_service_api.dto;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {
    private String id;
    private String userId;
    private String content;
    private String imageUrl;
    private Date createdAt;
    private String title;
    private List<String> tags;
}