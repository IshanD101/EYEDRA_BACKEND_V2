package com.eyedra.community_service_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageReqDto {

    @NotBlank(message = "Content is mandatory!!")
    @Size(max = 150, message = "Message content can't exceed 150 characters!!")
    private String content;
    private String senderName;
}
