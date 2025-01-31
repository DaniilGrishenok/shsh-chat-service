package com.shsh.chat_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PersonalMessageResponse {

    private String messageId;
    private String chatId;
    private String senderId;
    private String recipientId;
    private String content;
    private String messageType;
    private String timestamp;
    private String status;
    private String parentMessageId;
    
}
