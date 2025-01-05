package com.shsh.chat_service.service;

import com.shsh.chat_service.dto.PersonalMessageRequest;
import com.shsh.chat_service.model.MessageStatus;
import com.shsh.chat_service.model.PersonalMessage;
import com.shsh.chat_service.repository.PersonalMessageRepository;
import com.shsh.chat_service.util.IdGeneratorService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service

@RequiredArgsConstructor
public class MessageService {
    private static final Logger log = LoggerFactory.getLogger(MessageService.class);
    private final PersonalMessageRepository personalMessageRepository;
    private final IdGeneratorService idGenerator;
    @Transactional
    public PersonalMessage savePersonalMessage(PersonalMessageRequest request) {

        PersonalMessage message = new PersonalMessage(idGenerator.generatePersonalMessageId());
        message.setContent(request.getContent());
        message.setSenderId(request.getSenderId());
        message.setChatId(request.getChatId());
        message.setRecipientId(request.getRecipientId());
        message.setStatus(MessageStatus.SENT);
        message.setMessageType("TEXT");


        personalMessageRepository.save(message);
        return message;
    }
    @Transactional
    public PersonalMessage savePhotoPersonalMessage(PersonalMessageRequest request) {

        PersonalMessage photoMessage = new PersonalMessage(idGenerator.generatePersonalMessageId());
        photoMessage.setContent(request.getContent());
        photoMessage.setSenderId(request.getSenderId());
        photoMessage.setChatId(request.getChatId());
        photoMessage.setRecipientId(request.getRecipientId());
        photoMessage.setStatus(MessageStatus.SENT);
        photoMessage.setMessageType("PHOTO");


        personalMessageRepository.save(photoMessage);
        return photoMessage;
    }
    @Transactional
    public void updatePersonalMessageStatusToDelivered(List<String> messagesIds) {
        validateMessagesIds(messagesIds);
        try {
            personalMessageRepository.updateStatusByIdIn(messagesIds, MessageStatus.DELIVERED);
            log.info("Updated {} messages to DELIVERED status");
        } catch (Exception e) {
            log.error("Failed to update message status to DELIVERED for messages: {}", messagesIds, e);
            throw new RuntimeException("Could not update message status to DELIVERED", e);
        }
    }

    @Transactional
    public void updatePersonalMessageStatusToRead(List<String> messagesIds) {
        validateMessagesIds(messagesIds);
        try {
            personalMessageRepository.updateStatusByIdIn(messagesIds, MessageStatus.READ);
            log.info("Updated {} messages to READ status");
        } catch (Exception e) {
            log.error("Failed to update message status to READ for messages: {}", messagesIds, e);
            throw new RuntimeException("Could not update message status to READ", e);
        }
    }

    private void validateMessagesIds(List<String> messagesIds) {
        if (messagesIds == null || messagesIds.isEmpty()) {
            log.warn("Empty message IDs list provided");
            throw new IllegalArgumentException("Message IDs list cannot be null or empty");
        }
    }

    @Transactional(readOnly = true)
    public List<PersonalMessage> getAllMessagesInChat(String chatId) {
        return personalMessageRepository.findByChatId(chatId);
    }
    @Transactional(readOnly = true)
    public List<PersonalMessage> getAllMeвssagesInChat(String chatId) {
        return personalMessageRepository.findByChatId(chatId);
    }
}
