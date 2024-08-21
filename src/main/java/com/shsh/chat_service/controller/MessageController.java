package com.shsh.chat_service.controller;

import com.shsh.chat_service.model.PersonalMessage;
import com.shsh.chat_service.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {

 private final MessageService messageService;

    @PutMapping("/status/delivered")
    public ResponseEntity<String> updateMessagesToDelivered(@RequestBody List<String> messageIds) {
        try {
            messageService.updatePersonalMessageStatusToDelivered(messageIds);
            return ResponseEntity.ok("Message statuses updated to DELIVERED");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to update message statuses to DELIVERED");
        }
    }
    @GetMapping("/getAllMessagesInChat")
    public ResponseEntity<List<PersonalMessage>> getAllMessagesInPersonalChat(
            @RequestParam String chatId) {
        List<PersonalMessage> messages = messageService.getAllMessagesInChat(chatId);
        return ResponseEntity.ok(messages);
    }


    @PutMapping("/status/read")
    public ResponseEntity<String> updateMessagesToRead(@RequestBody List<String> messageIds) {
        try {
            messageService.updatePersonalMessageStatusToRead(messageIds);
            return ResponseEntity.ok("Message statuses updated to READ");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to update message statuses to READ");
        }
    }
}
