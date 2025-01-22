package com.shsh.chat_service.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
@Log4j2
public class CustomHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        try {
            String userId = UriComponentsBuilder.fromUri(request.getURI())
                    .build()
                    .getQueryParams()
                    .getFirst("userId");

            // Валидация параметра
            if (userId != null && !userId.trim().isEmpty()) {
                attributes.put("userId", userId);
                log.info("UserId extracted: {}", userId);
            } else {
                log.warn("Invalid or missing userId. Query: {}", request.getURI().getQuery());
                return false;
            }
        } catch (Exception e) {
            log.error("Error while extracting userId: {}", e.getMessage(), e);
            return false;
        }
        return true; // Разрешаем соединение
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        if (exception != null) {
            log.error("Error during handshake: {}", exception.getMessage(), exception);
        } else {
            log.info("Handshake completed successfully with URI: {}", request.getURI());
        }
    }
}
