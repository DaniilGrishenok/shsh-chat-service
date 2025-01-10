package com.shsh.chat_service.service;

import com.shsh.chat_service.dto.FileResponseDTO;
import com.shsh.chat_service.model.PersonalMessage;
import com.shsh.chat_service.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final S3Service s3Service;

    // Загружаем фото
    public String uploadPhoto(String chatId, MultipartFile file) throws IOException {
        return s3Service.uploadFile(chatId, file);
    }

    // Получаем фото
    public byte[] downloadFile(String chatId, String fileName) throws IOException {
        return s3Service.downloadFile(chatId, fileName).readAllBytes();
    }

    // Проверка наличия фото
    public boolean fileExists(String chatId, String fileName) {
        return s3Service.fileExists(chatId, fileName);
    }

    // Удаляем фото
    public void deletePhoto(String chatId, String fileName) {
        s3Service.deleteFileFromS3(chatId, fileName);
    }
//    @Transactional
//    public void deletePhotosByChatId(String chatId) {
//        try {
//            // Получение списка сообщений с типом "PHOTO"
//            List<PersonalMessage> photoMessages = personalMessageRepository.findByChatId(chatId).stream()
//                    .filter(msg -> "PHOTO".equals(msg.getMessageType()))
//                    .collect(Collectors.toList());
//
//            // Удаление фото из S3
//            for (PersonalMessage message : photoMessages) {
//                s3Service.deleteFile(message.getContent());
//            }
//
//            log.info("Все фотографии из чата с ID {} успешно удалены", chatId);
//        } catch (Exception e) {
//            log.error("Ошибка при удалении фотографий из чата с ID {}", chatId, e);
//            throw new RuntimeException("Ошибка при удалении фотографий: " + e.getMessage(), e);
//        }
//    }

}
