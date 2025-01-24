package com.shsh.chat_service.model;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TypingEvent {
    private String chatId;
    private String userId;
    private ChatEventType type;
    private LocalDateTime timestamp;
}
enum ChatEventType {
    TYPING_STARTED,
    TYPING_STOPPED
}