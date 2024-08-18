package com.shsh.chat_service.service;


import com.shsh.chat_service.dto.CreateOneToOneChatResponse;
import com.shsh.chat_service.model.PersonalChat;
import com.shsh.chat_service.repository.PersonalChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final PersonalChatRepository chatRepository;
    /*
    * CRUD чат персональный
    * CRUD чат групповой
    * Получить все сообщения в чате
    * получить список всех чатов где есть этот пользвоатель userID
    * генерация уникального id
    *
    *
    *
    *
    *
    *
    *  */
    private ResponseEntity<CreateOneToOneChatResponse> createPersonalChat(String firstUserId,
                                                                          String firstUserId){
        PersonalChat chat = new PersonalChat(firstUserId, firstUserId);
        chat.setCreatedByUserId(firstUserId);
    }

}