package com.shsh.chat_service.controller;

import com.shsh.chat_service.dto.DeleteMessageRequest;
import com.shsh.chat_service.dto.EditMessageRequest;
import com.shsh.chat_service.dto.ErrorResponse;
import com.shsh.chat_service.dto.TypingRequest;
import com.shsh.chat_service.model.ChatEvent;
import com.shsh.chat_service.model.PersonalMessage;
import com.shsh.chat_service.model.TypingEvent;
import com.shsh.chat_service.service.ChatEventService;
import com.shsh.chat_service.service.ChatService;
import com.shsh.chat_service.service.MessageService;
import com.shsh.chat_service.service.TypingStatusService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
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
    private final ChatService chatService;
    private final TypingStatusService typingStatusService;

    @MessageMapping("/events/typing")
    public void handleTypingEvent(@Payload TypingRequest request) {
        try {
            final String chatId = request.getChatId();
            final String userId = request.getInitiatorUserId();
            log.info("РЕКВЕСТ ПЕЧАТАЕТ {}", request);
            // 1. Проверка прав доступа
            if (!chatService.isUserParticipant(chatId, userId)) {
                throw new SecurityException("User is not a participant of the chat");
            }
            // 2. Обработка статуса через сервис
            typingStatusService.processTypingEvent(chatId, userId, request.isTyping());

            // 3. Отправка текущего статуса
            ChatEvent event = ChatEvent.builder()
                    .type(ChatEvent.EventType.TYPING_INDICATOR)
                    .chatId(chatId)
                    .initiatorId(userId)
                    .payload(Map.of(
                            "isTyping", request.isTyping(),
                            "timeoutMs", request.isTyping() ? 3000 : 0
                    ))
                    .timestamp(LocalDateTime.now())
                    .build();
            log.info("ИВЕНТ ПЕЧАТАЕТ {}", event);
            sendEventToParticipantsExcludingInitiator(event);

        } catch (Exception e) {
            handleError(request, e);
        }
    }

    private void sendEventToParticipantsExcludingInitiator(ChatEvent event) {
        chatService.getChatParticipants(event.getChatId()).stream()
                .filter(participantId -> !participantId.equals(event.getInitiatorId()))
                .forEach(participantId -> {
                    messagingTemplate.convertAndSendToUser(
                            participantId,
                            "/queue/events",
                            event
                    );
                });
    }

    private void handleError(TypingRequest request, Exception e) {
        ErrorResponse error = new ErrorResponse(
                "TYPING_ERROR",
                e.getMessage(),
                LocalDateTime.now(),
                Map.of(
                        "chatId", request.getChatId(),
                        "userId", request.getInitiatorUserId()
                )
        );

        messagingTemplate.convertAndSendToUser(
                request.getInitiatorUserId(),
                "/queue/errors",
                error
        );

        log.error("Typing error: {}", e.getMessage());
    }

    @MessageMapping("/events/deletePersonalMessage")
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


    @MessageMapping("/events/editPersonalMessage")
    public void handleEditMessage(@Valid @Payload EditMessageRequest request) {
        try {
            PersonalMessage editedMessage = messageService.editMessage(request);

            ChatEvent event = ChatEvent.builder()
                    .type(ChatEvent.EventType.MESSAGE_EDITED)
                    .chatId(request.getChatId())
                    .initiatorId(request.getSenderId())
                    .targetMessageId(editedMessage.getMessageId())
                    .payload(Map.of(
                            "content", editedMessage.getContent(),
                            "editedAt", editedMessage.getEditedAt()
                    ))
                    .timestamp(LocalDateTime.now())
                    .build();

            sendEventToParticipants(event);

        } catch (EntityNotFoundException ex) {
            sendError(request.getSenderId(), "MESSAGE_NOT_FOUND", ex.getMessage());
        } catch (SecurityException ex) {
            sendError(request.getSenderId(), "EDIT_UNAUTHORIZED", "Только автор может редактировать сообщение");
        } catch (UnsupportedOperationException ex) {
            sendError(request.getSenderId(), "EDIT_FORBIDDEN", ex.getMessage());
        } catch (Exception ex) {
            log.error("Ошибка редактирования: {}", ex.getMessage());
            sendError(request.getSenderId(), "EDIT_ERROR", "Внутренняя ошибка сервера");
        }
    }

    private void sendError(String userId, String errorCode, String message) {
        ErrorResponse error = new ErrorResponse(
                errorCode,
                message,
                LocalDateTime.now()
        );

        messagingTemplate.convertAndSendToUser(
                userId,
                "/queue/errors",
                error
        );
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
