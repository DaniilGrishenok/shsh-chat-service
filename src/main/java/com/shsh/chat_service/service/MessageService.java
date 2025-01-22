package com.shsh.chat_service.service;

import com.shsh.chat_service.dto.MediaResponseDTO;
import com.shsh.chat_service.dto.PersonalMessageRequest;
import com.shsh.chat_service.dto.PersonalMessageResponse;
import com.shsh.chat_service.model.MessageStatus;
import com.shsh.chat_service.model.PersonalMessage;
import com.shsh.chat_service.repository.PersonalMessageRepository;
import com.shsh.chat_service.util.IdGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class MessageService {

    private final PersonalMessageRepository personalMessageRepository;
    private final IdGeneratorService idGenerator;

    @Transactional(readOnly = true)
    public PersonalMessageResponse getMessageById(String messageId) {

        PersonalMessage message = personalMessageRepository.findByMessageId(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with ID " + messageId + " not found"));


        return new PersonalMessageResponse(
                message.getMessageId(),
                message.getChatId(),
                message.getSenderId(),
                message.getRecipientId(),
                message.getContent(),
                message.getMessageType(),
                message.getTimestamp().toString(),
                message.getStatus().toString(),
                message.getParentMessageId()
        );
    }
    @Transactional
    public PersonalMessage savePersonalMessage(PersonalMessageRequest request) throws NoSuchAlgorithmException {

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
    @Transactional(readOnly = true)
    public List<MediaResponseDTO> getPhotosByChatId(String chatId) {
        List<PersonalMessage> photoMessages = personalMessageRepository.findByChatIdAndMessageType(chatId, "PHOTO");


        return photoMessages.stream()
                .map(message -> new MediaResponseDTO(
                        message.getMessageId(),
                        message.getTimestamp().toString(),
                        message.getContent(),
                        message.getSenderId()
                ))
                .collect(Collectors.toList());
    }


    @Transactional
    public PersonalMessage saveMessage(PersonalMessageRequest request) throws NoSuchAlgorithmException {

        PersonalMessage message = new PersonalMessage(idGenerator.generatePersonalMessageId());
        message.setContent(request.getContent());
        message.setSenderId(request.getSenderId());
        message.setChatId(request.getChatId());
        message.setRecipientId(request.getRecipientId());
        message.setStatus(MessageStatus.SENT);
        message.setMessageType(request.getMessageType());

        personalMessageRepository.save(message);
        return message;
    }

    @Transactional
    public PersonalMessage savePhotoPersonalMessage(PersonalMessageRequest request) throws NoSuchAlgorithmException {

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
    public PersonalMessage createReplyMessage(String chatId, String senderId, String recipientId,
                                              String content, String parentMessageId, String messageType) throws NoSuchAlgorithmException {
        PersonalMessage parentMessage = personalMessageRepository.findById(parentMessageId)
                .orElseThrow(() -> new RuntimeException("Сообщение-родитель не найдено"));

        PersonalMessage replyMessage = new PersonalMessage(idGenerator.generatePersonalMessageId());
        replyMessage.setChatId(chatId);
        replyMessage.setSenderId(senderId);
        replyMessage.setRecipientId(recipientId);
        replyMessage.setContent(content);
        replyMessage.setMessageType(messageType);
        replyMessage.setStatus(MessageStatus.SENT);
        replyMessage.setParentMessageId(parentMessageId);

        return personalMessageRepository.save(replyMessage);
    }

    @Transactional
    public void deleteMessagesByChatId(String chatId) {
        try {
            List<PersonalMessage> messages = personalMessageRepository.findByChatId(chatId);

            if (messages.isEmpty()) {
                log.warn("Нет сообщений для удаления в чате с ID {}", chatId);
                return;
            }

            personalMessageRepository.deleteAll(messages);
            log.info("Все сообщения из чата с ID {} успешно удалены", chatId);
        } catch (Exception e) {
            log.error("Ошибка при удалении сообщений из чата с ID {}", chatId, e);
            throw new RuntimeException("Ошибка при удалении сообщений: " + e.getMessage(), e);
        }
    }


    @Transactional
    public void deleteMessagesByIds(List<String> messageIds) {
        try {
            if (messageIds == null || messageIds.isEmpty()) {
                log.warn("Пустой список ID сообщений для удаления");
                return;
            }

            List<PersonalMessage> messages = personalMessageRepository.findAllById(messageIds);

            if (messages.isEmpty()) {
                log.warn("Нет сообщений для удаления с указанными ID {}", messageIds);
                return;
            }

            personalMessageRepository.deleteAll(messages);
            log.info("Сообщения с ID {} успешно удалены", messageIds);
        } catch (Exception e) {
            log.error("Ошибка при удалении сообщений с ID {}", messageIds, e);
            throw new RuntimeException("Ошибка при удалении сообщений: " + e.getMessage(), e);
        }
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
