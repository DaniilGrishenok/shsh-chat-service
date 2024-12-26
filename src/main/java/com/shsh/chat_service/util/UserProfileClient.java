package com.shsh.chat_service.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class UserProfileClient {
    private final RestTemplate restTemplate;
    private final String userProfileServiceUrl = "lb://USER-PROFILE-SERVICE";  // URL вашего микросервиса

    public boolean checkUserExists(String userId) {
        String url = userProfileServiceUrl + "/ups/api/" + userId + "/exists";

        try {
            return restTemplate.getForObject(url, Boolean.class);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при проверке существования пользователя: " + e.getMessage(), e);
        }
    }
}
