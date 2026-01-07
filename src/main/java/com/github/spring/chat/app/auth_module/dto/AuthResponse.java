package com.github.spring.chat.app.auth_module.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthResponse(
        String token,
        String type,
        String id,
        String username,
        String email,
        String displayName
) {
    public AuthResponse(String token, String id, String username, String email, String displayName) {
        this(token, "Bearer", id, username, email, displayName);
    }
}