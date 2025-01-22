package com.shsh.chat_service.controller;

import com.shsh.chat_service.dto.DeleteMessageRequest;
import com.shsh.chat_service.dto.ErrorResponse;
import com.shsh.chat_service.model.ChatEvent;
import com.shsh.chat_service.service.ChatEventService;
import com.shsh.chat_service.service.ChatService;
import com.shsh.chat_service.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@Log4j2
public class EventsController {
    private final MessageService messageService;
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatEventService eventService;
    private final ChatService chatService;

    @MessageMapping("/events/delete")
    public void handleDeleteEvent(@Payload DeleteMessageRequest request) {
        try {
            messageService.deleteMessagesByIds(request.getMessageIds());

            request.getMessageIds().forEach(messageId -> {
                ChatEvent event = ChatEvent.builder()
                        .type(ChatEvent.EventType.MESSAGE_DELETED)
                        .chatId(request.getChatId())
                        .initiatorId(request.getInitiatorUserId())
                        .targetMessageId(messageId)
                        .timestamp(LocalDateTime.now())
                        .build();

                sendEventToParticipants(event);
            });

        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse(
                    "MESSAGE_DELETION_FAILED",
                    e.getMessage(),
                    LocalDateTime.now(),
                    Map.of("chatId", request.getChatId())
            );

            messagingTemplate.convertAndSendToUser(
                    request.getInitiatorUserId(),
                    "/queue/errors",
                    error
            );
        }
    }

    private void sendEventToParticipants(ChatEvent event) {
        Set<String> participants = chatService.getChatParticipants(event.getChatId());

        participants.forEach(userId -> {
            messagingTemplate.convertAndSendToUser(
                    userId,
                    "/queue/events",
                    event
            );
        });
    }
    //    @MessageMapping("/events/reaction")
//    public void handleReactionEvent(@Payload ReactionRequest request) {
//        ChatEvent event = ChatEvent.builder()
//                .type(ChatEvent.EventType.REACTION_ADDED)
//                .chatId(request.getChatId())
//                .initiatorId(request.getUserId())
//                .targetMessageId(request.getMessageId())
//                .payload(request.getEmoji())
//                .timestamp(LocalDateTime.now())
//                .build();
//
//        sendEventToParticipants(event);
//    }
}
