package com.shsh.chat_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDto {
    private String id;
    private String user1Id;
    private String user2Id;
    private LocalDateTime createdAt; 
//    private String lastMessage;    // Последнее сообщение в чате (опционально)
//    private String lastMessageTimestamp; // Временная метка последнего сообщения (опционально)
}