package com.shsh.chat_service.controller;

import com.shsh.chat_service.dto.PersonalMessageRequest;
import com.shsh.chat_service.model.MessageStatus;
import com.shsh.chat_service.model.PersonalMessage;
import com.shsh.chat_service.service.MessageService;

import lombok.RequiredArgsConstructor;
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

import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@Log4j2
public class WebSocketController {

    private final MessageService messageService;
    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/ping")
    public void handlePing(@Payload String ping, Message<?> message) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        String userId = accessor.getSessionAttributes() != null
                ? (String) accessor.getSessionAttributes().get("userId")
                : "Unknown";

        log.info("Получен ping от пользователя: {}", userId);
    }

    @MessageMapping("/send")
    public void sendMessage(@Payload PersonalMessageRequest messageRequest) {
        PersonalMessage textMessage = messageService.savePersonalMessage(messageRequest);
        messagingTemplate.convertAndSendToUser(
                textMessage.getRecipientId(),
                "/queue/messages",
                textMessage
        );
    }
    @MessageMapping("/send/photo")
    public void sendPhotoMessage(@Payload PersonalMessageRequest photoMessageRequest) {

        PersonalMessage photoMessage = messageService.savePhotoPersonalMessage(photoMessageRequest);
        messagingTemplate.convertAndSendToUser(
                photoMessage.getRecipientId(),
                "/queue/messages",
                photoMessage
        );
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