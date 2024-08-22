package com.shsh.chat_service.repository;

import com.shsh.chat_service.model.Chat;
import com.shsh.chat_service.model.PersonalChat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.Optional;

@Repository
public interface PersonalChatRepository extends MongoRepository<PersonalChat, String> {


    // Найти чаты, где пользователь является либо user1, либо user2
    List<PersonalChat> findByUser1IdOrUser2Id(String user1Id, String user2Id);
}