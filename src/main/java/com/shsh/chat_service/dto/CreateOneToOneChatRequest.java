package com.shsh.chat_service.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
public class CreateOneToOneChatRequest {
    private String firstUserId;
    private String secondUserId;
}

