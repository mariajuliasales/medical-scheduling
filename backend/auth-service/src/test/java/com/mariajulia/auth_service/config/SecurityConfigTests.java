package com.mariajulia.auth_service.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Security Configuration Tests")
class SecurityConfigTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Should have BCryptPasswordEncoder bean available")
    void testPasswordEncoderBeanAvailable() {
        assertNotNull(passwordEncoder);
    }

    @Test
    @DisplayName("Should encode password with BCrypt")
    void testPasswordEncoding() {
        String rawPassword = "TestPassword123!";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
        assertTrue(encodedPassword.startsWith("$2a$") || encodedPassword.startsWith("$2y$"));
    }

    @Test
    @DisplayName("Should not match incorrect password")
    void testPasswordNotMatchingIncorrectPassword() {
        String rawPassword = "TestPassword123!";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        String wrongPassword = "WrongPassword456!";

        assertFalse(passwordEncoder.matches(wrongPassword, encodedPassword));
    }

    @Test
    @DisplayName("Same password should produce different hashes")
    void testPasswordEncodingProduceDifferentHashes() {
        String rawPassword = "TestPassword123!";
        String encoded1 = passwordEncoder.encode(rawPassword);
        String encoded2 = passwordEncoder.encode(rawPassword);

        assertNotEquals(encoded1, encoded2);
        assertTrue(passwordEncoder.matches(rawPassword, encoded1));
        assertTrue(passwordEncoder.matches(rawPassword, encoded2));
    }

    @Test
    @DisplayName("BCrypt strength should be 12")
    void testBCryptStrength() {
        String password1 = "TestPassword123!";
        String password2 = "TestPassword123!";
        String encoded1 = passwordEncoder.encode(password1);
        String encoded2 = passwordEncoder.encode(password2);

        // Different hashes for the same password (due to salt)
        assertNotEquals(encoded1, encoded2);
        // But both should match the original password
        assertTrue(passwordEncoder.matches(password1, encoded1));
        assertTrue(passwordEncoder.matches(password2, encoded2));
    }
}
