package com.shsh.chat_service.repository;


import com.shsh.chat_service.model.PersonalMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonalMessageRepository extends MongoRepository<PersonalMessage, String> {

}