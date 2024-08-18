package com.shsh.chat_service.model;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Document(collection = "personal_chats")
public class PersonalChat extends Chat{

    private final String user1Id;
    private final String user2Id;


    public PersonalChat(String user1Id, String user2Id) {
        this.user1Id = user1Id;
        this.user2Id = user2Id;

    }


}
