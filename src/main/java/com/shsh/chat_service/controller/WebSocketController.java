package com.shsh.chat_service.controller;

import com.shsh.chat_service.model.Message;
import com.shsh.chat_service.service.MessageService;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final MessageService messageService;
    private final SimpMessageSendingOperations messagingTemplate;
    //private final WebSocketSessionService webSocketSessionService;
    // TODO: 15.08.2024 сделать проверки на метод sendmessage()
    @MessageMapping("/send")
    public void sendMessage(SimpMessageHeaderAccessor sha, @Payload Message message) {
        System.out.println("Username: "+ Objects.requireNonNull(sha.getUser()).getName());
        messageService.saveMessage(message);
        messagingTemplate.convertAndSendToUser(
                message.getRecipientId(),
                "/queue/messages",
                message
        );
    }
    @PostMapping()
    @GetMapping("/")
    public String index() {
        return "index";
    }


}
