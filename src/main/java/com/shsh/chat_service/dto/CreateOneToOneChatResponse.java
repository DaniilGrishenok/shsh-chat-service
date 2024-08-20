package com.shsh.chat_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateOneToOneChatResponse {
    private String chatId;
    private String firstUserId;
    private String secondUserId;
    private boolean createdSuccessfully;
}
