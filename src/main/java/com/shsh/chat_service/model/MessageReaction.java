package com.shsh.chat_service.model;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "message_reactions")
@Data
public class MessageReaction {
    @Id
    private String id;
    private String messageId;
    private String userId;
    private String reaction;
    private LocalDateTime timestamp;


    public MessageReaction() {
        this.timestamp = LocalDateTime.now();
    }
}