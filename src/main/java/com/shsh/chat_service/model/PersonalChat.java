package com.shsh.chat_service.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "personal_chats")
public class PersonalChat extends Chat{

    private final String user1Id;
    private final String user2Id;

    public PersonalChat(String user1Id, String user2Id) {

        super();
        this.user1Id = user1Id;
        this.user2Id = user2Id;


    }


}
