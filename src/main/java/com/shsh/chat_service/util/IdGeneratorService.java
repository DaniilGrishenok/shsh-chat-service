package com.shsh.chat_service.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Component
public class IdGeneratorService {

    private static final int UUID_SHORT_LENGTH = 20;
    private static final int HASH_LENGTH = 20;
    private static final int HASH_LENGTH_MESSAGE = 28;

    public String generatePersonalChatId(String user1Id, String user2Id) throws NoSuchAlgorithmException {

        String firstUserId = user1Id.compareTo(user2Id) < 0 ? user1Id : user2Id;
        String secondUserId = firstUserId.equals(user1Id) ? user2Id : user1Id;


        String uuid = UUID.randomUUID().toString().substring(0, UUID_SHORT_LENGTH);
        long timestamp = System.currentTimeMillis();


        String rawId = "PC-" + firstUserId + "-" + secondUserId + "-" + timestamp + "-" + uuid;


        String hashedId = hashString(rawId);

        return "PC-" + hashedId;
    }


    public String generatePersonalMessageId() throws NoSuchAlgorithmException {
        String uuid = UUID.randomUUID().toString().substring(0, UUID_SHORT_LENGTH);
        long timestamp = System.currentTimeMillis();

        String rawId = "PM-" + timestamp + "-" + uuid;

        String hashedId = hashStringMessage(rawId);

        return "PM-" + hashedId;
    }

    private String hashString(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString().substring(0, HASH_LENGTH);
    }
    private String hashStringMessage(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }


        return sb.toString().substring(0, HASH_LENGTH_MESSAGE);
    }
}
