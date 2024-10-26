package com.shsh.chat_service.config;

import com.shsh.chat_service.model.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Log4j2
public class StompUserInterceptor implements ChannelInterceptor {

    private final RedisTemplate<String, String> redisTemplate;

    public StompUserInterceptor(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            // Пытаемся извлечь userId из атрибутов WebSocket-сессии
            Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
            String userId = sessionAttributes != null ? (String) sessionAttributes.get("userId") : null;

            // Если userId в атрибутах отсутствует, проверяем заголовок "X-User-Id"
            if (userId == null) {
                userId = accessor.getFirstNativeHeader("X-User-Id");
            }

            if (userId != null) {
                accessor.setUser(new User(userId));
                redisTemplate.opsForValue().set("online:" + userId, "true");
                log.info("User connected: " + userId);
                log.info("User added to Redis: " + "online:" + userId);
            } else {
                log.warn("User ID is null during CONNECT");
            }
        } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            User user = (User) accessor.getUser();
            if (user != null) {
                redisTemplate.delete("online:" + user.getName());
                log.info("User disconnected: " + user.getName());
                log.info("User removed from Redis: " + "online:" + user.getName());
            } else {
                log.warn("User is null during DISCONNECT");
            }
        }

        return message;
    }
}
