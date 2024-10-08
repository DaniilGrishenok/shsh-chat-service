package com.shsh.chat_service.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IdGeneratorService {

    // Генерация уникального ID для личного чата на основе ID пользователей
    public String generatePersonalChatId(String user1Id, String user2Id) {
        // Пример: комбинирование ID пользователей и случайного UUID для гарантии уникальности
        return user1Id + "-" + user2Id + "-" + UUID.randomUUID().toString();
    }

    public String generateGroupChatId() {
        // Пример: просто случайный UUID
        return "group-" + UUID.randomUUID().toString();
    }

    public String generatePersonalMessageId() {
        return UUID.randomUUID().toString();
    }
}