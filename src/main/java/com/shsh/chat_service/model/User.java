package com.shsh.chat_service.model;

import java.security.Principal;

public class User implements Principal {
    private final String userId;
    public User(String userId) {
        this.userId = userId;
    }
    @Override
    public String getName() {
        return userId;
    }
}