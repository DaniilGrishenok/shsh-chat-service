package com.shsh.chat_service.repository;

import com.shsh.chat_service.model.Chat;
import com.shsh.chat_service.model.PersonalChat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.Optional;

public interface PersonalChatRepository extends MongoRepository<PersonalChat, String> {


    // Найти чаты, где пользователь является либо user1, либо user2
    List<PersonalChat> findByUser1IdOrUser2Id(String user1Id, String user2Id);
    @Query("{ $or: [ { $and: [ { 'user1Id': ?0 }, { 'user2Id': ?1 } ] }, { $and: [ { 'user1Id': ?1 }, { 'user2Id': ?0 } ] } ] }")
    Optional<PersonalChat> findByUserIds(String user1Id, String user2Id);
    Optional<PersonalChat> findByChatId(String chatId);
}