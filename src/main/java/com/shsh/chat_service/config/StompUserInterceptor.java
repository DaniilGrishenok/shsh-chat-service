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

        // Извлекаем userId из строки запроса
        String userId = stompHeaderAccessor.getSessionAttributes().get("userId") != null ?
                (String) stompHeaderAccessor.getSessionAttributes().get("userId") :
                null;

        // Логируем, если не удалось извлечь userId
        if (userId == null) {
            log.warn("Не удалось извлечь userId из строки запроса. Соединение может быть не авторизовано.");
        } else {
            log.info("Извлечен userId: {}", userId);
        }

        if (userId != null) {
            StompCommand stompCommand = stompHeaderAccessor.getCommand();
            if (stompCommand == StompCommand.CONNECT) {
                // Логируем успешное подключение
                log.info("Пользователь с userId {} подключился.", userId);
                // Соединение установлено, сохраняем информацию о подключении в Redis
                redisTemplate.opsForValue().set("user:" + userId, "connected");
                log.debug("Сохранена информация о подключении пользователя в Redis: user:{}", userId);
            } else if (stompCommand == StompCommand.DISCONNECT) {
                // Логируем разрыв соединения
                log.info("Пользователь с userId {} отключился.", userId);
                // Соединение разорвано, удаляем информацию о пользователе
                redisTemplate.delete("user:" + userId);
                log.debug("Удалена информация о пользователе из Redis: user:{}", userId);
            } else {
                log.debug("Необрабатываемая команда STOMP: {}", stompCommand);
            }
        }

        return message;
    }
}