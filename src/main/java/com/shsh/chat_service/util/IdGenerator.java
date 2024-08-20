package com.shsh.chat_service.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IdGenerator {

    // Генерация уникального ID для личного чата на основе ID пользователей
    public String generatePersonalChatId(String user1Id, String user2Id) {
        // Пример: комбинирование ID пользователей и случайного UUID для гарантии уникальности
        return user1Id + "-" + user2Id + "-" + UUID.randomUUID().toString();
    }

    // Генерация уникального ID для группового чата
    public String generateGroupChatId() {
        // Пример: просто случайный UUID
        return "group-" + UUID.randomUUID().toString();
    }
}