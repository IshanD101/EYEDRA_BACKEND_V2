package com.eyedra.post_service_api.dto;

import lombok.Data;
import java.util.List;

@Data
public class PostSearchDTO {
    private String title;
    private List<String> tags;
    private String author;
    private String startDate;
    private String endDate;
}
