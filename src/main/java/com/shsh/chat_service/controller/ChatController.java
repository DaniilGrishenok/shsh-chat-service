package com.shsh.chat_service.controller;

import com.shsh.chat_service.dto.ChatDto;
import com.shsh.chat_service.dto.CreateOneToOneChatRequest;
import com.shsh.chat_service.dto.CreateOneToOneChatResponse;
import com.shsh.chat_service.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chats")
public class ChatController {
    private final ChatService chatService;
    @PostMapping("/createOneToOneChat")
    public ResponseEntity<CreateOneToOneChatResponse> createOneToOneChat(@RequestBody CreateOneToOneChatRequest request){
        var firstUserId = request.getFirstUserId();
        var secondUserId = request.getSecondUserId();
        var response = chatService.createPersonalChat(firstUserId, secondUserId);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/allChats")
    public ResponseEntity<List<ChatDto>> getChatsForUser(@RequestParam String userId) {
        List<ChatDto> chats = chatService.getAllChatsForUser(userId);
        return ResponseEntity.ok(chats);
    }
    @DeleteMapping("/{chatId}")
    public ResponseEntity<String> deleteChat(@PathVariable String chatId) {
        try {
            chatService.deleteChat(chatId);
            return ResponseEntity.ok("Чат и связанные данные успешно удалены.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при удалении чата: " + e.getMessage());
        }
    }


    //aff3a126-0ecb-4f73-bd37-11a032fb6b82


}
