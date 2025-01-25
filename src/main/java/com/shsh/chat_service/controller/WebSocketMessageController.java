package com.shsh.chat_service.controller;

import com.shsh.chat_service.service.ChatService;
import com.shsh.chat_service.service.MessageService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@Log4j2
public class WebSocketMessageController {

    private final MessageService messageService;
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService chatService;
    @MessageMapping("/messages/deliver")
    public void handleDeliveryConfirmation(@Payload DeliveryConfirmation confirmation) {
        messageService.markAsDelivered(confirmation.getMessageIds(), confirmation.getRecipientId());
    }

    @MessageMapping("/messages/read")
    public void handleReadConfirmation(@Payload ReadConfirmation confirmation) {
        List<String> readIds = messageService.markAsRead(
                confirmation.getMessageIds(),
                confirmation.getReaderId()
        );

        Set<String> participants = chatService.getChatParticipants(
                confirmation.getChatId()
        );

        participants.stream()
                .filter(userId -> !userId.equals(confirmation.getReaderId()))
                .forEach(userId -> {
                    ReadEvent event = new ReadEvent(
                            readIds,
                            confirmation.getReaderId(),
                            confirmation.getChatId()
                    );

                    messagingTemplate.convertAndSendToUser(
                            userId,
                            "/queue/messages-read",
                            event
                    );
                });
    }


    // DTO классы
    @Data
    @NoArgsConstructor
    private static class DeliveryConfirmation {
        private List<String> messageIds;
        private String recipientId;
    }

    @Data
    @NoArgsConstructor
    private static class ReadConfirmation {
        private List<String> messageIds;
        private String readerId;
        private String chatId;
    }

    @Data
    @AllArgsConstructor
    private static class ReadEvent {
        private List<String> messageIds;
        private String readerId;
        private String chatId;
    }
}