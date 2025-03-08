package com.eyedra.post_service_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {
    private String title;
    private String content;
    private String author;

    private String userId; // Removed author field
}
