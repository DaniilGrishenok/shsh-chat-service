package com.shsh.chat_service.model;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document
@Data
public abstract class Message {
    @Id
    private String messageId;;
    private String chatId;
    private LocalDateTime timestamp;

    public Message(){

    }

}
