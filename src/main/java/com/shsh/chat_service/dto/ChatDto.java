package com.shsh.chat_service.dto;

import com.shsh.chat_service.model.MessageStatus;
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

}