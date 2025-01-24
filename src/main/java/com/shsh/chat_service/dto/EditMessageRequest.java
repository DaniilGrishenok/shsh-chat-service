package com.shsh.chat_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditMessageRequest {
    @NotBlank(message = "ID сообщения обязательно")
    private String messageId;

    @NotBlank(message = "ID чата обязательно")
    private String chatId;

    @NotBlank(message = "ID отправителя обязательно")
    private String senderId;

    @NotBlank(message = "Новый текст не может быть пустым")
    @Size(max = 2000, message = "Максимальная длина сообщения - 2000 символов")
    private String newContent;
}