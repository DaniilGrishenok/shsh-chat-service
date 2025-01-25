package com.shsh.chat_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Document(collection = "personal_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalMessage {
    @Id
    private String messageId;
    private String chatId;
    private LocalDateTime timestamp;
    private String senderId;
    private String recipientId;
    private String content;
    private String messageType;
    private MessageStatus status = MessageStatus.SENT;
    private String parentMessageId;
    private boolean isEdited;
    private LocalDateTime editedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime readAt;

    public PersonalMessage(String messageId) {
        this.messageId = messageId;
        this.timestamp = LocalDateTime.now();
    }
}