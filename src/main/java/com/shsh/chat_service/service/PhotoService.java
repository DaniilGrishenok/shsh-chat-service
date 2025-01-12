package com.shsh.chat_service.service;

import com.shsh.chat_service.dto.FileResponseDTO;
import com.shsh.chat_service.model.PersonalMessage;
import com.shsh.chat_service.repository.PersonalMessageRepository;
import com.shsh.chat_service.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class PhotoService {

    private final S3Service s3Service;
    private final PersonalMessageRepository personalMessageRepository;

    public String uploadPhoto(String chatId, MultipartFile file) throws IOException {
        return s3Service.uploadFile(chatId, file);
    }
    public byte[] downloadFile(String chatId, String fileName) throws IOException {
        return s3Service.downloadFile(chatId, fileName).readAllBytes();
    }

    public boolean fileExists(String chatId, String fileName) {
        return s3Service.fileExists(chatId, fileName);
    }


    public void deletePhoto(String chatId, String fileName) {
        s3Service.deleteFileFromS3(chatId, fileName);
    }

    @Transactional
    public void deletePhotosByChatId(String chatId) {
        try {

            List<PersonalMessage> photoMessages = personalMessageRepository.findByChatId(chatId).stream()
                    .filter(msg -> "PHOTO".equals(msg.getMessageType()))
                    .collect(Collectors.toList());

            if (photoMessages.isEmpty()) {
                log.warn("Нет сообщений с типом PHOTO для удаления в чате с ID {}", chatId);
                return;
            }

            for (PersonalMessage message : photoMessages) {
                String fileUrl = message.getContent();
                if (fileUrl != null) {
                    String fileName = extractFileNameFromUrl(fileUrl);
                    if (fileName != null) {
                        s3Service.deleteFileFromS3(chatId, fileName); // Удаляем файл из S3
                        log.info("Фотография с именем {} успешно удалена из S3", fileName);
                    } else {
                        log.warn("Не удалось извлечь имя файла из URL {}", fileUrl);
                    }
                } else {
                    log.warn("Сообщение с ID {} не содержит URL для удаления", message.getMessageId());
                }
            }

            personalMessageRepository.deleteAll(photoMessages);
            log.info("Все фотографии из чата с ID {} успешно удалены", chatId);

        } catch (Exception e) {
            log.error("Ошибка при удалении фотографий из чата с ID {}", chatId, e);
            throw new RuntimeException("Ошибка при удалении фотографий: " + e.getMessage(), e);
        }
    }


    private String extractFileNameFromUrl(String fileUrl) {
        try {
            String[] urlParts = fileUrl.split("/");
            return urlParts[urlParts.length - 1];
        } catch (Exception e) {
            log.error("Ошибка при извлечении имени файла из URL: {}", fileUrl, e);
            return null;
        }
    }


}
