package com.shsh.chat_service.EventListeners;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Log4j2
public class WebSocketEventListener {

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public WebSocketEventListener(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        log.info("Сессия {} разорвана.", sessionId);

        String userId = redisTemplate.opsForValue().get("session:" + sessionId);
        if (userId != null) {
            redisTemplate.opsForSet().remove("user:" + userId + ":sessions", sessionId);

            Long remainingSessions = redisTemplate.opsForSet().size("user:" + userId + ":sessions");
            if (remainingSessions == null || remainingSessions == 0) {
                redisTemplate.opsForValue().set("user:" + userId, "offline");
                log.info("Пользователь {} помечен как offline", userId);
            }
        }
    }
}
