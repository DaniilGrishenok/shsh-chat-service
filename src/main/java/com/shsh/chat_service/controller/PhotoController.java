package com.shsh.chat_service.controller;

import com.shsh.chat_service.dto.ErrorResponseDTO;
import com.shsh.chat_service.dto.FileResponseDTO;
import com.shsh.chat_service.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/cs/api/photos")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    // Загрузка фото
    @PostMapping("/upload/{chatId}")
    public ResponseEntity<Object> uploadPhoto(@PathVariable String chatId, @RequestParam("file") MultipartFile file) {

        try {
            // Загружаем файл
            String fileUrl = photoService.uploadPhoto(chatId, file);
            FileResponseDTO response = new FileResponseDTO(fileUrl, file.getOriginalFilename());
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            // Если ошибка при загрузке файла, возвращаем ошибку
            ErrorResponseDTO error = new ErrorResponseDTO("Ошибка при загрузке файла: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Получение фото по имени файла
    @GetMapping("/download/{chatId}/{fileName}")
    public ResponseEntity<Object> downloadPhoto(@PathVariable String chatId, @PathVariable String fileName) {
        try {

            if (!photoService.fileExists(chatId, fileName)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponseDTO("Файл не найден", HttpStatus.NOT_FOUND.value()));
            }

            byte[] fileContent = photoService.downloadFile(chatId, fileName);
            return ResponseEntity.ok(fileContent);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO("Ошибка при скачивании файла: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    // Удаление фото
    @DeleteMapping("/delete/{chatId}/{fileName}")
    public ResponseEntity<Object> deletePhoto(@PathVariable String chatId, @PathVariable String fileName) {
        try {
            if (!photoService.fileExists(chatId, fileName)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponseDTO("Файл не найден для удаления", HttpStatus.NOT_FOUND.value()));
            }
            photoService.deletePhoto(chatId, fileName);
            return ResponseEntity.ok("Файл успешно удален");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO("Ошибка при удалении файла: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }
}
