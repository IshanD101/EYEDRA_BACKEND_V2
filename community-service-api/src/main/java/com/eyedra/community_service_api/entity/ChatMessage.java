package com.eyedra.community_service_api.entity;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "chat_message")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {

    @Id
    private String id;

    private Long messageId;
    private Long communityId;
    private Long senderId;
    private String senderName;
    private String content;
    private LocalDateTime timestamp;
    private List<Long> readByUserIds;
}
