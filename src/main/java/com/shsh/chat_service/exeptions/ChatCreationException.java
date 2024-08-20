package com.shsh.chat_service.exeptions;

public class ChatCreationException extends RuntimeException {

    public ChatCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}