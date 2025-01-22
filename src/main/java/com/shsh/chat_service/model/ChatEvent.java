package com.shsh.chat_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatEvent {
    public enum EventType {
        MESSAGE_DELETED,
        REACTION_ADDED,
        TYPING_INDICATOR,
        MESSAGE_EDITED
    }

    private EventType type;
    private String chatId;
    private String initiatorId;
    private String targetMessageId;
    private String payload;
    private LocalDateTime timestamp;
}