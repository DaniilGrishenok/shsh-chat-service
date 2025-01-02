package com.shsh.chat_service.config;

import com.shsh.chat_service.model.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class StompUserInterceptor implements ChannelInterceptor {

    private final RedisTemplate<String, String> redisTemplate;

    public StompUserInterceptor(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(message);

        String userId = stompHeaderAccessor.getSessionAttributes().get("userId") != null ?
                (String) stompHeaderAccessor.getSessionAttributes().get("userId") : null;
        String sessionId = stompHeaderAccessor.getSessionId();

        if (userId == null) {
            log.warn("Не удалось извлечь userId. Соединение может быть не авторизовано.");
            return message;
        }

        log.info("Извлечен userId: {}, sessionId: {}", userId, sessionId);

        if (sessionId != null) {
            StompCommand stompCommand = stompHeaderAccessor.getCommand();
            try {
                switch (stompCommand) {
                    case CONNECT:
                        log.info("Пользователь с userId {} подключился. sessionId: {}", userId, sessionId);
                        redisTemplate.opsForSet().add("user:" + userId + ":sessions", sessionId);
                        redisTemplate.opsForValue().set("user:" + userId + ":lastPingAt", String.valueOf(System.currentTimeMillis()));
                        redisTemplate.opsForValue().set("user:" + userId, "online"); // Устанавливаем статус online
                        break;

                    case DISCONNECT:
                        log.info("Пользователь с userId {} отключился. sessionId: {}", userId, sessionId);
                        redisTemplate.opsForSet().remove("user:" + userId + ":sessions", sessionId);

                        Long remainingSessions = redisTemplate.opsForSet().size("user:" + userId + ":sessions");
                        if (remainingSessions == null || remainingSessions == 0) {
                            redisTemplate.opsForValue().set("user:" + userId, "offline");
                            log.info("Пользователь {} помечен как offline", userId);
                        }
                        break;

                    default:
                        redisTemplate.opsForValue().set("user:" + userId + ":lastPingAt", String.valueOf(System.currentTimeMillis()));
                        redisTemplate.opsForValue().set("user:" + userId, "online"); // Поддерживаем статус online
                        break;
                }

            } catch (Exception e) {
                log.error("Ошибка при обработке события STOMP для userId {} и sessionId {}: {}", userId, sessionId, e.getMessage());
            }
        }

        return message;
    }
}

