package com.shsh.chat_service.controller;

import com.shsh.chat_service.dto.DeleteMessageRequest;
import com.shsh.chat_service.dto.ErrorResponse;
import com.shsh.chat_service.dto.PersonalMessageRequest;
import com.shsh.chat_service.model.ChatEvent;
import com.shsh.chat_service.model.MessageStatus;
import com.shsh.chat_service.model.PersonalMessage;
import com.shsh.chat_service.service.ChatService;
import com.shsh.chat_service.service.MessageService;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@Log4j2
public class WebSocketController {

    private final MessageService messageService;
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/send")
    @SneakyThrows
    public void sendMessage(@Payload PersonalMessageRequest messageRequest)  {
        PersonalMessage message = messageService.saveMessage(messageRequest); 
//        messagingTemplate.convertAndSendToUser(
//                message.getRecipientId(),
//                "/queue/messages",
//                message
//        );
        sendToParticipants(message);
    }

    private void sendToParticipants(PersonalMessage messageRequest) {
        Set<String> participants = chatService.getChatParticipants(messageRequest.getChatId());

        participants.forEach(userId -> {
            messagingTemplate.convertAndSendToUser(
                    userId,
                    "/queue/events",
                    messageRequest
            );
        });
    }
    @MessageMapping("/send/reply")
    @SneakyThrows
    public void replyToMessage(@Payload PersonalMessageRequest messageRequest) {
        PersonalMessage replyMessage = messageService.createReplyMessage(
                messageRequest.getChatId(),
                messageRequest.getSenderId(),
                messageRequest.getRecipientId(),
                messageRequest.getContent(),
                messageRequest.getParentMessageId(),
                messageRequest.getMessageType()
        );
        messagingTemplate.convertAndSendToUser(
                replyMessage.getRecipientId(),
                "/queue/messages",
                replyMessage
        );
    }


    @MessageMapping("/send/photo")
    @SneakyThrows
    public void sendPhotoMessage(@Payload PersonalMessageRequest photoMessageRequest) {

        PersonalMessage photoMessage = messageService.savePhotoPersonalMessage(photoMessageRequest);
        messagingTemplate.convertAndSendToUser(
                photoMessage.getRecipientId(),
                "/queue/messages",
                photoMessage
        );
    }
    @MessageMapping("/ping")
    public void handlePing(@Payload String ping, Message<?> message) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String userId = accessor.getSessionAttributes() != null
                ? (String) accessor.getSessionAttributes().get("userId")
                : "Unknown";
    }
    @GetMapping("/home")
    public String index() {
        return "index";
    }

    @GetMapping("/chat")
    public String chatPage(@RequestParam String chatId, @RequestParam String userId, Model model) {
        model.addAttribute("chatId", chatId);
        model.addAttribute("userId", userId);
        return "chat";
    }

    @GetMapping("cs/api/getAllMessagesInChat")
    @ResponseBody
    public List<PersonalMessage> getAllMessagesInPersonalChat(@RequestParam String chatId) {
        return messageService.getAllMessagesInChat(chatId);
    }
}