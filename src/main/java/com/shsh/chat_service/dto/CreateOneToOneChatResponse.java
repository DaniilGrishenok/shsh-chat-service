package com.shsh.chat_service.dto;

import lombok.Data;
import lombok.Getter;
import org.springframework.web.service.annotation.GetExchange;
@Data
public class CreateOneToOneChatResponse {
    private String chatId;
    private String firstUserId;
    private String secondUserId;
    private boolean createdSuccessfully;
}
