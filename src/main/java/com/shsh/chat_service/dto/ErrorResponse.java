package com.shsh.chat_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String errorCode;
    private String message;
    private LocalDateTime timestamp; // Используем Instant вместо LocalDateTime
    private Map<String, Object> details = Collections.emptyMap(); // Дефолтное значение

    // Дополнительный конструктор для случаев без деталей
    public ErrorResponse(String errorCode, String message, LocalDateTime timestamp) {
        this(errorCode, message, timestamp, Collections.emptyMap());
    }
}