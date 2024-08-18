package com.shsh.chat_service.controller;

import com.shsh.chat_service.dto.CreateOneToOneChatRequest;
import com.shsh.chat_service.dto.CreateOneToOneChatResponse;
import com.shsh.chat_service.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    @PostMapping("chats/createOneToOneChat/")
    public ResponseEntity<CreateOneToOneChatResponse> createOneToOneChat(CreateOneToOneChatRequest request){
        var firstUserId = request.getFirstUserId();
        var secondUserId =request.getSecondUserId();
        return new ResponseEntity<>();

    }




}
