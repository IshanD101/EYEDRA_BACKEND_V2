package com.eyedra.user_service_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListenerApplicationReqDto {
    private String fullName;
    private String description;
    private MultipartFile certificationImage;
}
