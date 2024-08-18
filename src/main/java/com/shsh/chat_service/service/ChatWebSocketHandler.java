//package com.shsh.chat_service.service;
//
//import com.shsh.chat_service.model.Message;
//import com.shsh.chat_service.repository.MessageRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//@RequiredArgsConstructor
//public class ChatWebSocketHandler extends TextWebSocketHandler {
//
//    private final MessageRepository messageRepository;
//
//
//    private Map<String, WebSocketSession> userSessions = new HashMap<>(); // To keep track of user sessions
//
//    @Override
//    public void handleTextMessage(WebSocketSession session, TextMessage message) {
//        String payload = message.getPayload();
//
//        // Example payload processing
//        // {"type":"CHAT","content":"Hello","chatRoomId":"room1","senderId":"user1","receiverId":"user2"}
//        String[] parts = payload.split(",");
//        String type = parts[0].split(":")[1].replace("\"", "");
//        String content = parts[1].split(":")[1].replace("\"", "");
//        String chatRoomId = parts[2].split(":")[1].replace("\"", "");
//        String senderId = parts[3].split(":")[1].replace("\"", "");
//        String receiverId = parts[4].split(":")[1].replace("\"", "");
//
//        // Save message to the database
//        Message messageToSave = new Message();
//        messageToSave.setChatRoomId(chatRoomId);
//        messageToSave.setSenderId(senderId);
//        messageToSave.setReceiverId(receiverId);
//        messageToSave.setContent(content);
//        messageToSave.setTimestamp(System.currentTimeMillis());
//        messageRepository.save(messageToSave);
//
//        // Send message to the intended recipient if their session is available
//        WebSocketSession recipientSession = userSessions.get(receiverId);
//        if (recipientSession != null && recipientSession.isOpen()) {
//            try {
//                recipientSession.sendMessage(new TextMessage(payload));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) {
//        String userId = getUserIdFromSession(session); // метод получения userId из сессии
//        userSessions.put(userId, session);
//
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
//        // Logic to remove session from userSessions map
//    }
//}
