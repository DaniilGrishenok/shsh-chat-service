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
    private final long INACTIVITY_LIMIT_MS = 30000; // Лимит неактивности (30 секунд)

    public InactiveUserMonitor(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Scheduled(fixedRate = 10000) // Выполнять каждые 10 секунд
    public void checkInactiveUsers() {
        log.info("Запуск проверки на неактивных пользователей...");

        Set<String> keys = redisTemplate.keys("user:*:lastPingAt");
        if (keys == null || keys.isEmpty()) {
            log.info("Неактивные пользователи отсутствуют.");
            return;
        }

        long currentTime = System.currentTimeMillis();
        for (String key : keys) {
            try {
                String lastPingAt = redisTemplate.opsForValue().get(key);
                if (lastPingAt != null) {
                    long lastPingTime = Long.parseLong(lastPingAt);
                    if (currentTime - lastPingTime > INACTIVITY_LIMIT_MS) {
                        String userId = key.split(":")[1];
                        String userStatus = redisTemplate.opsForValue().get("user:" + userId);
                        // Если пользователь еще не помечен как offline, то меняем статус
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