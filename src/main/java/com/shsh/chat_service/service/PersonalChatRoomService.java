//package com.shsh.chat_service.service;
//
//import com.shsh.chat_service.model.ChatRoom;
//import com.shsh.chat_service.repository.ChatRoomRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class PersonalChatRoomService {
//    private final ChatRoomRepository chatRoomRepository;
//
//    // Создать личную комнату
////    public ChatRoom createPersonalChatRoom(String userId1, String userId2) {
////        // Логика для создания личной комнаты, возможно, с уникальным идентификатором
////        String roomId = generateUniqueRoomId(userId1, userId2);
////        ChatRoom chatRoom = new ChatRoom(roomId, name, Arrays.asList(userId1, userId2), false); // isGroup = false для личных комнат
////        return chatRoomRepository.save(chatRoom);
////    }
//
//    // Получить список всех личных комнат пользователя
//    public List<ChatRoom> getPersonalChatRooms(String userId) {
//        return chatRoomRepository.findByParticipantsContaining(userId); // Метод findByParticipantsContaining должен использоваться для поиска по списку участников
//    }
//
//    // Получить личную комнату по идентификатору
//    public ChatRoom getChatRoom(String chatRoomId) {
//        return chatRoomRepository.findById(chatRoomId).orElse(null);
//    }
//
//    // Удалить личную комнату
//    public boolean deleteChatRoom(String chatRoomId) {
//        if (chatRoomRepository.existsById(chatRoomId)) {
//            chatRoomRepository.deleteById(chatRoomId);
//            return true;
//        }
//        return false;
//    }
//
//    private String generateUniqueRoomId(String userId1, String userId2) {
//        // Генерация уникального идентификатора комнаты
//        return userId1.compareTo(userId2) < 0 ? userId1 + "-" + userId2 : userId2 + "-" + userId1; // Генерация уникального идентификатора
//    }
//}
