package com.shsh.chat_service.config;

import com.shsh.chat_service.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
@Component
@Log4j2
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtUtils jwtUtils;

    public JwtHandshakeInterceptor(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        log.info("beforeHandshake called");
        String token = request.getURI().getQuery().replaceAll("token=", "");
        log.info("Received token: {}", token);  // Логирование полученного токена

        // Если токен не пустой, извлекаем userId
        if (token != null && !token.isEmpty()) {
            String userId = jwtUtils.extractUserIdFromToken(token);
            log.info("Extracted userId: {}", userId);  // Логирование извлеченного userId

            if (userId != null) {
                attributes.put("userId", userId);
                log.info("User ID added to attributes: {}", userId);  // Логирование добавленного userId в атрибуты
            } else {
                log.warn("User ID is null after extraction");  // Логирование предупреждения, если userId null
            }
        } else {
            log.warn("Token is null or empty");  // Логирование предупреждения, если токен пуст
        }

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }

//    @Override
//    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
//                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
//
//        // Извлечение токена из заголовка Authorization
//        String authHeader = request.getHeaders().getFirst("Authorization");
//
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
//            String userId = jwtUtils.extractUserIdFromToken(token);
//            if (userId != null) {
//                attributes.put("userId", userId);
//                request.getHeaders().add("X-User-Id", userId); // Пробуем добавить заголовок напрямую
//            }
//        }
//        return true;
//    }

}