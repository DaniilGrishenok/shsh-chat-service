package com.shsh.chat_service.dto;

import com.shsh.chat_service.model.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class ChatAndLastMessageDTO {

    private String id;
    private String user1Id;
    private String user2Id;
    private LocalDateTime createdAt;
    private MessagePreview lastMessage;
    @Data
    @AllArgsConstructor
    public static class MessagePreview {
        private String content;
        private LocalDateTime timestamp;
        private MessageStatus status;
        private String messageType;
    }

}
