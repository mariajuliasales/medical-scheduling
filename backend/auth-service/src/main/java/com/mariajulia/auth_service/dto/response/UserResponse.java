package com.mariajulia.auth_service.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record UserResponse(
        UUID id,
        String name,
        String email,
        String role,
        boolean active,
        LocalDateTime createdAt) {
}