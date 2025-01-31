package com.shsh.chat_service.service;


import com.shsh.chat_service.dto.ChatAndLastMessageDTO;
import com.shsh.chat_service.dto.ChatDto;
import com.shsh.chat_service.dto.CreateOneToOneChatResponse;
import com.shsh.chat_service.exeptions.ChatCreationException;
import com.shsh.chat_service.model.PersonalChat;
import com.shsh.chat_service.model.PersonalMessage;
import com.shsh.chat_service.repository.PersonalChatRepository;
import com.shsh.chat_service.repository.PersonalMessageRepository;
import com.shsh.chat_service.util.IdGeneratorService;
import com.shsh.chat_service.util.UserProfileClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ChatService {

    private final PersonalChatRepository personalChatRepository;
    private final IdGeneratorService idGeneratorService;
    private final UserProfileClient userProfileClient;
    private final MessageService messageService;
    private final PhotoService photoService;
    private final PersonalMessageRepository personalMessageRepository;
    @Transactional
    public CreateOneToOneChatResponse createPersonalChat(String firstUserId, String secondUserId) {
        try {
            // Проверяем существование пользователей
            if (!userProfileClient.checkUserExists(firstUserId) || !userProfileClient.checkUserExists(secondUserId)) {
                return new CreateOneToOneChatResponse(null, firstUserId, secondUserId, false, "Один или оба пользователя не существуют.");
            }

            Optional<PersonalChat> existingChat = personalChatRepository.findByUserIds(firstUserId, secondUserId);
            if (existingChat.isPresent()) {
                return new CreateOneToOneChatResponse(existingChat.get().getId(), firstUserId, secondUserId, false, "Чат с этими пользователями уже существует!");
            }

            // Генерация ID чата
            String chatId = idGeneratorService.generatePersonalChatId(firstUserId, secondUserId);
            PersonalChat chat = new PersonalChat(firstUserId, secondUserId);
            chat.setId(chatId);
            personalChatRepository.save(chat);

            // Возвращаем успешный ответ с ID чата
            return new CreateOneToOneChatResponse(chat.getId(), chat.getUser1Id(), chat.getUser2Id(), true, null);

        } catch (Exception e) {
            log.error("Ошибка при создании чата: {}", e.getMessage(), e);
            return new CreateOneToOneChatResponse(null, firstUserId, secondUserId, false, "Не удалось создать личный чат: " + e.getMessage());
        }
    }

    public boolean isUserParticipant(String chatId, String userId) {
        return getChatParticipants(chatId).contains(userId);
    }

    public List<ChatDto> getAllChatsForUser(String userId) {
        List<PersonalChat> chats = personalChatRepository.findByUser1IdOrUser2Id(userId, userId);
        return chats.stream()
                .map(this::convertToChatDto)
                .collect(Collectors.toList());
    }
    public List<ChatAndLastMessageDTO> getAllChatsForUser1(String userId) {
        List<PersonalChat> chats = personalChatRepository.findByUser1IdOrUser2Id(userId, userId);

        List<String> chatIds = chats.stream()
                .map(PersonalChat::getId)
                .collect(Collectors.toList());

        Map<String, PersonalMessage> lastMessages = personalMessageRepository
                .findLastMessagesByChatIds(chatIds)
                .stream()
                .collect(Collectors.toMap(
                        PersonalMessage::getChatId,
                        Function.identity()
                ));

        return chats.stream()
                .map(chat -> convertToChatDto(chat, lastMessages.get(chat.getId())))
                .collect(Collectors.toList());
    }
    private ChatAndLastMessageDTO convertToChatDto(PersonalChat chat, PersonalMessage lastMessage) {
        ChatAndLastMessageDTO dto = new ChatAndLastMessageDTO();
        dto.setId(chat.getId());
        dto.setUser1Id(chat.getUser1Id());
        dto.setUser2Id(chat.getUser2Id());
        dto.setCreatedAt(chat.getCreatedAt());

        if (lastMessage != null) {
            dto.setLastMessage(new ChatAndLastMessageDTO.MessagePreview(
                    lastMessage.getContent(),
                    lastMessage.getTimestamp(),
                    lastMessage.getStatus(),
                    lastMessage.getMessageType()
            ));
        }

        return dto;
    }

    @Transactional
    public void deleteChat(String chatId) {
        try {
            photoService.deletePhotosByChatId(chatId);
            messageService.deleteMessagesByChatId(chatId);
            personalChatRepository.deleteById(chatId);

            log.info("Чат с ID {} и связанные данные успешно удалены", chatId);
        } catch (Exception e) {
            log.error("Ошибка при удалении чата с ID {}", chatId, e);

            throw new RuntimeException("Ошибка при удалении чата: " + e.getMessage(), e);
        }
    }

    public Set<String> getChatParticipants(String chatId) {
        return personalChatRepository.findById(chatId)
                .map(chat -> Set.of(chat.getUser1Id(), chat.getUser2Id()))
                .orElseThrow(() -> new RuntimeException("Chat not found"));
    }

    private ChatDto convertToChatDto(PersonalChat chat) {
        return new ChatDto(
                chat.getId(),
                chat.getUser1Id(),
                chat.getUser2Id(),
                chat.getCreatedAt()
        );
    }

}