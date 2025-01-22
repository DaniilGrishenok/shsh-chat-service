package com.shsh.chat_service.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Document(collection = "personal_messages")
@Data
public class PersonalMessage{
    @Id
    private String messageId;
    private String chatId;
    private LocalDateTime timestamp;
    private String senderId;
    private String recipientId;
    private String content;
    private String messageType;
    private MessageStatus status;
    private String parentMessageId;
    public PersonalMessage(String messageId){
        this.timestamp = LocalDateTime.now();
        this.messageId = messageId;
    }

}