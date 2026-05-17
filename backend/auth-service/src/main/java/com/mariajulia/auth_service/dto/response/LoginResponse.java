package com.mariajulia.auth_service.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record LoginResponse(
    String token,
    String type, // type = "Bearer" no service
    UUID userId,
    String email,
    String role,
    long expiresIn
)
{}
