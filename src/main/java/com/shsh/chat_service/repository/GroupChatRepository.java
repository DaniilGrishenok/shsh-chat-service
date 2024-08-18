package com.shsh.chat_service.repository;

import com.shsh.chat_service.model.GroupChat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupChatRepository extends MongoRepository<GroupChat, String> {




}
