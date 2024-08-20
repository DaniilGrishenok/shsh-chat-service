package com.shsh.chat_service.service;

import com.shsh.chat_service.model.PersonalMessage;
import com.shsh.chat_service.repository.PersonalMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service

@RequiredArgsConstructor
public class MessageService {

    private final PersonalMessageRepository personalMessageRepository;
    @Transactional
    public void saveMessage(PersonalMessage message) {
        personalMessageRepository.save(message);
    }

}
