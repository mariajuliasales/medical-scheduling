package com.mariajulia.auth_service.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
        "jwt.secret=test-secret-key-that-is-long-enough-for-hs256-algorithm",
        "jwt.expiration=3600000"
})
@DisplayName("JwtProvider Security Tests")
class JwtProviderTests {

    @Autowired
    private JwtProvider jwtProvider;

    private UUID testUserId;
    private String testEmail;
    private String testRole;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testEmail = "test@example.com";
        testRole = "PATIENT";
    }

    @Test
    @DisplayName("Should generate valid JWT token")
    void testGenerateToken() {
        String token = jwtProvider.generateToken(testUserId, testEmail, testRole);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
    }

    @Test
    @DisplayName("Should extract email from token")
    void testGetUserEmailFromToken() {
        String token = jwtProvider.generateToken(testUserId, testEmail, testRole);
        String extractedEmail = jwtProvider.getUserEmailFromToken(token);

        assertEquals(testEmail, extractedEmail);
    }

    @Test
    @DisplayName("Should extract userId from token")
    void testGetUserIdFromToken() {
        String token = jwtProvider.generateToken(testUserId, testEmail, testRole);
        UUID extractedId = jwtProvider.getUserIdFromToken(token);

        assertEquals(testUserId, extractedId);
    }

    @Test
    @DisplayName("Should extract role from token")
    void testGetUserRoleFromToken() {
        String token = jwtProvider.generateToken(testUserId, testEmail, testRole);
        String extractedRole = jwtProvider.getUserRoleFromToken(token);

        assertEquals(testRole, extractedRole);
    }

    @Test
    @DisplayName("Should validate correct token")
    void testValidateCorrectToken() {
        String token = jwtProvider.generateToken(testUserId, testEmail, testRole);
        boolean isValid = jwtProvider.validateToken(token);

        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should reject invalid token")
    void testValidateInvalidToken() {
        String invalidToken = "invalid.token.here";
        boolean isValid = jwtProvider.validateToken(invalidToken);

        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should detect non-expired token")
    void testIsTokenExpiredWithValidToken() {
        String token = jwtProvider.generateToken(testUserId, testEmail, testRole);
        boolean isExpired = jwtProvider.isTokenExpired(token);

        assertFalse(isExpired);
    }

    @Test
    @DisplayName("Should generate different tokens for different users")
    void testGenerateDifferentTokensForDifferentUsers() {
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();

        String token1 = jwtProvider.generateToken(userId1, "user1@example.com", "PATIENT");
        String token2 = jwtProvider.generateToken(userId2, "user2@example.com", "DOCTOR");

        assertNotEquals(token1, token2);
        assertEquals(userId1, jwtProvider.getUserIdFromToken(token1));
        assertEquals(userId2, jwtProvider.getUserIdFromToken(token2));
    }
}
