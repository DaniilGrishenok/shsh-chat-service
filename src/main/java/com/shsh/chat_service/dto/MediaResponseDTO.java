package com.shsh.chat_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MediaResponseDTO {
    private String messageId;
    private String timestamp;
    private String photoUrl;
    private String senderId;
}

