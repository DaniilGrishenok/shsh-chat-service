package com.shsh.chat_service.service;

import com.shsh.chat_service.model.Message;
import com.shsh.chat_service.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;



    public void saveMessage(Message message) {
        messageRepository.save(message);
    }

}
