package com.shsh.chat_service.service;

import com.shsh.chat_service.model.ChatEvent;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TypingStatusService {
    private final ChatService chatService;
    private final SimpMessageSendingOperations messagingTemplate;

    private final ConcurrentMap<String, ScheduledFuture<?>> timers = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

    public void processTypingEvent(String chatId, String userId, boolean isTyping) {
        final String key = buildKey(chatId, userId);
        cancelExistingTimer(key);

        if (isTyping) {
            scheduleAutoStop(chatId, userId, key);
        }
    }

    private void scheduleAutoStop(String chatId, String userId, String key) {
        ScheduledFuture<?> timer = scheduler.schedule(() -> {
            try {
                sendStopEvent(chatId, userId);
            } finally {
                timers.remove(key);
            }
        }, 3, TimeUnit.SECONDS);

        timers.put(key, timer);
    }

    private void sendStopEvent(String chatId, String userId) {
        ChatEvent event = ChatEvent.builder()
                .type(ChatEvent.EventType.TYPING_INDICATOR)
                .chatId(chatId)
                .initiatorId(userId)
                .payload(Map.of(
                        "isTyping", false,
                        "timeoutMs", 0
                ))
                .timestamp(LocalDateTime.now())
                .build();

        chatService.getChatParticipants(chatId).stream()
                .filter(participantId -> !participantId.equals(userId))
                .forEach(participantId -> {
                    messagingTemplate.convertAndSendToUser(
                            participantId,
                            "/queue/events",
                            event
                    );
                });
    }

    private void sendToReceiver(String receiverId, ChatEvent event) {
        messagingTemplate.convertAndSendToUser(
                receiverId,
                "/queue/events",
                event
        );
    }

    public void clearUserTimers(String userId) {
        timers.keySet().removeIf(key -> key.endsWith(":" + userId));
    }

    private void cancelExistingTimer(String key) {
        ScheduledFuture<?> timer = timers.remove(key);
        if (timer != null) {
            timer.cancel(false);
        }
    }

    private String buildKey(String chatId, String userId) {
        return chatId + ":" + userId;
    }

    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void cleanupStaleTimers() {
        timers.keySet().removeIf(key ->
                timers.get(key).isDone() ||
                        timers.get(key).isCancelled()
        );
    }

    @PreDestroy
    public void shutdown() {
        scheduler.shutdownNow();
    }
}