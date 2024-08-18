//package com.shsh.chat_service.service;
//
//import org.springframework.stereotype.Service;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Service
//public class WebSocketSessionService {
//
//    private final ConcurrentHashMap<String, String> userSessionMap = new ConcurrentHashMap<>();
//
//    public void registerSession(String userId, String sessionId) {
//        userSessionMap.put(userId, sessionId);
//    }
//
//    public String getSessionId(String userId) {
//        return userSessionMap.get(userId);
//    }
//
//    public void removeSession(String sessionId) {
//        userSessionMap.values().remove(sessionId);
//    }
//}
