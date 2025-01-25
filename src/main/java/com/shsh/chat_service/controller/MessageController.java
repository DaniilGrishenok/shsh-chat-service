package com.shsh.chat_service.controller;

import com.shsh.chat_service.dto.PersonalMessageResponse;
import com.shsh.chat_service.model.MessageStatus;
import com.shsh.chat_service.model.PersonalMessage;
import com.shsh.chat_service.repository.PersonalMessageRepository;
import com.shsh.chat_service.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
@Log4j2
public class MessageController {

 private final MessageService messageService;
 private final PersonalMessageRepository personalMessageRepository;

    @GetMapping("/getSentMessages")
    public ResponseEntity<List<PersonalMessage>> getUnreadMessages(
            @RequestParam String recipientId) {
        List<PersonalMessage> messages = personalMessageRepository
                .findByRecipientIdAndStatus(recipientId, MessageStatus.SENT);
        return ResponseEntity.ok(messages);
    }
    @GetMapping("/{messageId}")
    public ResponseEntity<?> getMessageById(@PathVariable String messageId) {
        try {

            PersonalMessage messageDTO = messageService.getMessageById(messageId);
            return ResponseEntity.ok(messageDTO);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Message not found", "messageId", messageId));
        } catch (Exception e) {
            log.error("Ошибка при получении сообщения с ID {}: {}", messageId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error", "details", e.getMessage()));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMessages(@RequestBody List<String> messageIds) {
        try {
            messageService.deleteMessagesByIds(messageIds);
            return ResponseEntity.ok("Сообщения успешно удалены");
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Ошибка при удалении сообщений: " + e.getMessage());
        }
    }

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


}
