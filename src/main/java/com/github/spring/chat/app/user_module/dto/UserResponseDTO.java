package com.github.spring.chat.app.user_module.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.spring.chat.app.auth_module.enums.UserStatus;
import com.github.spring.chat.app.auth_module.model.User;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponseDTO(
        String id,
        String username,
        String email,
        String displayName,
        String avatarUrl,
        String bio,
        UserStatus status,
        Instant lastSeen,
        Instant createdAt
) {
    public UserResponseDTO(User user) {
        this(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getDisplayName(),
                user.getAvatarUrl(),
                user.getBio(),
                user.getStatus(),
                user.getLastSeen(),
                user.getCreatedAt()
        );
    }

}