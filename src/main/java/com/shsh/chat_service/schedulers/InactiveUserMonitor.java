package com.shsh.chat_service.schedulers;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
@Component
@Log4j2
public class InactiveUserMonitor {

    private final RedisTemplate<String, String> redisTemplate;

    public InactiveUserMonitor(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Scheduled(fixedRate = 16000)
    public void checkInactiveUsers() {
        Set<String> keys = redisTemplate.keys("user:*:lastPingAt");
        if (keys == null || keys.isEmpty()) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        for (String key : keys) {
            try {
                String lastPingAt = redisTemplate.opsForValue().get(key);
                if (lastPingAt != null) {
                    long lastPingTime = Long.parseLong(lastPingAt);
                    long INACTIVITY_LIMIT_MS = 15000;
                    if (currentTime - lastPingTime > INACTIVITY_LIMIT_MS) {
                        String userId = key.split(":")[1];
                        String userStatus = redisTemplate.opsForValue().get("user:" + userId);
                        if (!"offline".equals(userStatus)) {
                            redisTemplate.opsForValue().set("user:" + userId, "offline");
                            log.info("Пользователь {} помечен как offline из-за неактивности.", userId);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("Ошибка при обработке ключа {}: {}", key, e.getMessage());
            }
        }
    }
}