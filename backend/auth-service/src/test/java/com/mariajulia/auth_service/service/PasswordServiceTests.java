package com.mariajulia.auth_service.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Password Service Tests")
class PasswordServiceTests {

    @Autowired
    private PasswordService passwordService;

    @Test
    @DisplayName("Should encode password with BCrypt")
    void testEncodePassword() {
        String rawPassword = "MySecurePassword123!";
        String encodedPassword = passwordService.encodePassword(rawPassword);

        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
    }

    @Test
    @DisplayName("Should validate correct password")
    void testValidateCorrectPassword() {
        String rawPassword = "MySecurePassword123!";
        String encodedPassword = passwordService.encodePassword(rawPassword);

        assertTrue(passwordService.validatePassword(rawPassword, encodedPassword));
    }

    @Test
    @DisplayName("Should reject incorrect password")
    void testValidateIncorrectPassword() {
        String rawPassword = "MySecurePassword123!";
        String wrongPassword = "WrongPassword456!";
        String encodedPassword = passwordService.encodePassword(rawPassword);

        assertFalse(passwordService.validatePassword(wrongPassword, encodedPassword));
    }
}
