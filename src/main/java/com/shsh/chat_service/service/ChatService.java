package com.shsh.chat_service.service;


import com.shsh.chat_service.dto.ChatDto;
import com.shsh.chat_service.dto.CreateOneToOneChatResponse;
import com.shsh.chat_service.exeptions.ChatCreationException;
import com.shsh.chat_service.model.PersonalChat;
import com.shsh.chat_service.repository.PersonalChatRepository;
import com.shsh.chat_service.util.IdGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final PersonalChatRepository personalChatRepository;
    private final IdGeneratorService idGeneratorService;


    @Transactional
    public CreateOneToOneChatResponse createPersonalChat(String firstUserId, String secondUserId) {
        try {
            // Генерация уникального идентификатора чата для личного чата
            String chatId = idGeneratorService.generatePersonalChatId(firstUserId, secondUserId);

            // Создание нового экземпляра PersonalChat
            PersonalChat chat = new PersonalChat(firstUserId, secondUserId);
            chat.setId(chatId); // Установка сгенерированного ID

            // Сохранение чата в репозитории
            personalChatRepository.save(chat);

            // Создание объекта ответа
            CreateOneToOneChatResponse response = new CreateOneToOneChatResponse(
                    chat.getId(),
                    chat.getUser1Id(),
                    chat.getUser2Id(),
                    true
            );


            return new CreateOneToOneChatResponse(chat.getId(), chat.getUser1Id(), chat.getUser2Id(), true);

        } catch (Exception e) {
            throw new ChatCreationException("Не удалось создать личный чат: ", e);
        }
    }

    public List<ChatDto> getAllChatsForUser(String userId) {
        List<PersonalChat> chats = personalChatRepository.findByUser1IdOrUser2Id(userId, userId);
        return chats.stream()
                .map(this::convertToChatDto)
                .collect(Collectors.toList());
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