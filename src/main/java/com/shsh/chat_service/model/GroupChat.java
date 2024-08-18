package com.shsh.chat_service.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "group_chats")
public class GroupChat extends Chat{

}
