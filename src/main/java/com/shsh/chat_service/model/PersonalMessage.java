package com.shsh.chat_service.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "personal_messages")
@Data
public class PersonalMessage extends Message{


    private String senderId;
    private String recipientId;
    private String content;

    private MessageStatus status;
    public PersonalMessage(String messageId){
        super(messageId);
    }

}