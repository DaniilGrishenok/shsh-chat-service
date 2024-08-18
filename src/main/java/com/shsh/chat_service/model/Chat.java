package com.shsh.chat_service.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



@Data
@Document
public abstract class Chat {
    @Id
    private String id;
    @CreatedDate
    private LocalDateTime createdAt;
    private String createdByUserId;

}