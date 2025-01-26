package com.shsh.chat_service.controller;

import com.shsh.chat_service.dto.ChatDto;
import com.shsh.chat_service.dto.CreateOneToOneChatRequest;
import com.shsh.chat_service.dto.CreateOneToOneChatResponse;
import com.shsh.chat_service.dto.MediaResponseDTO;
import com.shsh.chat_service.service.ChatService;
import com.shsh.chat_service.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chats")
@Log4j2
public class ChatController {
    private final ChatService chatService;
    private final MessageService messageService;

    @GetMapping("/{chatId}/photos")
    public ResponseEntity<List<MediaResponseDTO>> getChatPhotos(@PathVariable String chatId) {
        try {

            List<MediaResponseDTO> photos = messageService.getPhotosByChatId(chatId);

            if (photos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(photos);

        } catch (Exception e) {

            log.error("Ошибка при получении фотографий для чата {}: {}", chatId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PostMapping("/createOneToOneChat")
    public ResponseEntity<CreateOneToOneChatResponse> createOneToOneChat(@RequestBody CreateOneToOneChatRequest request) {
        var firstUserId = request.getFirstUserId();
        var secondUserId = request.getSecondUserId();
        CreateOneToOneChatResponse response = chatService.createPersonalChat(firstUserId, secondUserId);

        if (!response.isChatCreated()) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteChat/{chatId}")
    public ResponseEntity<String> deleteChat(@PathVariable String chatId) {
        try {
            chatService.deleteChat(chatId);
            return ResponseEntity.ok("Чат и связанные данные успешно удалены.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при удалении чата: " + e.getMessage());
        }
    }

    @GetMapping("/allChats")
    public ResponseEntity<List<ChatDto>> getChatsForUser(@RequestParam String userId) {
        List<ChatDto> chats = chatService.getAllChatsForUser(userId);
        return ResponseEntity.ok(chats);
    }
    @GetMapping("/v2/allChats")
    public ResponseEntity<List<?>> getChatsForUserV2(@RequestParam String userId) {
        var chats = chatService.getAllChatsForUser1(userId);
        return ResponseEntity.ok(chats);
    }
}
