//package com.shsh.chat_service.service;
//
//import com.shsh.chat_service.model.ChatRoom;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class GroupChatRoomService {
//    private final ChatRoomRepository chatRoomRepository;
//
//    // Создать групповую комнату
//    public ChatRoom createGroupChatRoom(String creatorId, List<String> userIds) {
//        // Логика для создания групповой комнаты, возможно, с уникальным идентификатором
//        String roomId = generateUniqueRoomId(creatorId);
//        String name = "";
//        ChatRoom chatRoom = new ChatRoom(roomId, name, userIds, false);
//        return chatRoomRepository.save(chatRoom);
//    }
//
//    // TODO: 08.08.2024
////    // Получить список всех групповых комнат пользователя
////    public List<ChatRoom> getGroupChatRooms(String userId) {
////        return chatRoomRepository.findByUserIdsContaining(userId);
////    }
//
//    // Получить групповую комнату по идентификатору
//    public ChatRoom getChatRoom(String chatRoomId) {
//        return chatRoomRepository.findById(chatRoomId).orElse(null);
//    }
//
//    // Удалить групповую комнату
//    public boolean deleteChatRoom(String chatRoomId) {
//        if (chatRoomRepository.existsById(chatRoomId)) {
//            chatRoomRepository.deleteById(chatRoomId);
//            return true;
//        }
//        return false;
//    }
//// TODO: 08.08.2024
////    // Добавить пользователей в групповую комнату
//
//
//// TODO: 08.08.2024
////    // Удалить пользователя из групповой комнаты
//
//
//    private String generateUniqueRoomId(String creatorId) {
//        // Генерация уникального идентификатора комнаты
//        return creatorId + "-" + System.currentTimeMillis(); // Можно использовать более сложный механизм
//    }
//
//    public List<ChatRoom> getGroupChatRooms(String userId) {
//        return new ArrayList<>();
//    }
//}