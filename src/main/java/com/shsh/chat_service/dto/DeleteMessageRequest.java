package com.shsh.chat_service.dto;

import lombok.Data;

import java.util.List;


@Data
public class DeleteMessageRequest {
    private String chatId;
    private String initiatorUserId;
    private List<String> messageIds;
}