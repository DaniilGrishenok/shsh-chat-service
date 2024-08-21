package com.shsh.chat_service.repository;


import com.shsh.chat_service.model.MessageStatus;
import com.shsh.chat_service.model.PersonalMessage;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonalMessageRepository extends MongoRepository<PersonalMessage, String> {

    @Modifying
    @Query("{ '_id': { $in: ?1 } }")
    void updateStatusByIdIn(List<String> ids, MessageStatus status);
    List<PersonalMessage> findByChatId(String chatId);
}