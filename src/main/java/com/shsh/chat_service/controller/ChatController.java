package com.shsh.chat_service.controller;

import com.shsh.chat_service.dto.CreateOneToOneChatRequest;
import com.shsh.chat_service.dto.CreateOneToOneChatResponse;
import com.shsh.chat_service.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    @PostMapping("/chats/createOneToOneChat/")
    public ResponseEntity<CreateOneToOneChatResponse> createOneToOneChat(@RequestBody CreateOneToOneChatRequest request){
        var firstUserId = request.getFirstUserId();
        var secondUserId = request.getSecondUserId();
        var response = chatService.createPersonalChat(firstUserId, secondUserId);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/home")
    public String index() {
        return "index";
    }





}
