package com.shsh.chat_service.service;

import com.shsh.chat_service.dto.FileResponseDTO;
import com.shsh.chat_service.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
}
