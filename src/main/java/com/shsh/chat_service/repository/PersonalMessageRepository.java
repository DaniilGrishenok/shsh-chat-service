package com.shsh.chat_service.repository;


import com.shsh.chat_service.model.MessageStatus;
import com.shsh.chat_service.model.PersonalMessage;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonalMessageRepository extends MongoRepository<PersonalMessage, String> {

    @Modifying
    @Query("{ '_id': { $in: ?1 } }")
    void updateStatusByIdIn(List<String> ids, MessageStatus status);
    List<PersonalMessage> findByChatId(String chatId);
    List<PersonalMessage> findByChatIdAndMessageType(String chatId, String messageType);

    List<PersonalMessage> findByRecipientIdAndStatus(String recipientId, MessageStatus status);

    @Aggregation(pipeline = {
            "{ $match: { chatId: { $in: ?0 } } }",
            "{ $sort: { timestamp: -1 } }",
            "{ $group: { _id: '$chatId', lastMessage: { $first: '$$ROOT' } } }"
    })
    List<PersonalMessage> findLastMessagesByChatIds(List<String> chatIds);

    @Query("{ 'messageId': ?0 }")
    Optional<PersonalMessage> findByMessageId(String messageId);

    @Query("{ 'messageId' : { $in: ?0 } }")
    List<PersonalMessage> findAllByIds(List<String> messageIds);
}