package com.shsh.chat_service.model;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
public abstract class Message {
    @Id
    private String id;
    private String chatId;
    private LocalDateTime timestamp;

    public Message(String messageId){
        this.timestamp = LocalDateTime.now();
        this.id = messageId;
    }

}
