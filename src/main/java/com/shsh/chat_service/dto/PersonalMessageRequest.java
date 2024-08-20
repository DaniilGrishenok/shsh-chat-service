package com.shsh.chat_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonalMessageRequest {
    private String chatId;
    private String userSenderId;
    private String userReciptentId;
    private String content;

}
