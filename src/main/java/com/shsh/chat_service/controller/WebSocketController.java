package com.shsh.chat_service.controller;

import com.shsh.chat_service.dto.PersonalMessageRequest;
import com.shsh.chat_service.model.MessageStatus;
import com.shsh.chat_service.model.PersonalMessage;
import com.shsh.chat_service.service.MessageService;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
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
public class WebSocketController {

    private final MessageService messageService;
    private final SimpMessageSendingOperations messagingTemplate;

    // TODO: 15.08.2024 сделать проверки на метод sendmessage()
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