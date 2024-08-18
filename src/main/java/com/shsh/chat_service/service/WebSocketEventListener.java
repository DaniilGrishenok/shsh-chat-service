//package com.shsh.chat_service.service;
//
//import org.springframework.context.event.EventListener;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.messaging.SessionConnectEvent;
//import org.springframework.web.socket.messaging.SessionDisconnectEvent;
//
//@Component
//public class WebSocketEventListener {
//
//    private final WebSocketSessionService webSocketSessionService;
//
//    public WebSocketEventListener(WebSocketSessionService webSocketSessionService) {
//        this.webSocketSessionService = webSocketSessionService;
//    }
//
//    @EventListener
//    public void handleWebSocketConnectListener(SessionConnectEvent event) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
//        String userId = accessor.getNativeHeader("userId").get(0); // Предполагаем, что userId передается в заголовке
//        String sessionId = accessor.getSessionId();
//        webSocketSessionService.registerSession(userId, sessionId);
//    }
//
//    @EventListener
//    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
//        String sessionId = event.getSessionId();
//        webSocketSessionService.removeSession(sessionId);
//    }
//}