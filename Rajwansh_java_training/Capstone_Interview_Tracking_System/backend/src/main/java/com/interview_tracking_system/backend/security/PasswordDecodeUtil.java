package com.interview_tracking_system.backend.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Utility to decode Base64 password from frontend
 */
public final class PasswordDecodeUtil {

    private PasswordDecodeUtil() {
    }

    public static String decodeBase64Password(String encodedPassword) {

        if (encodedPassword == null || encodedPassword.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        try {
            byte[] decodedBytes = Base64.getDecoder().decode(encodedPassword);
            return new String(decodedBytes, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid Base64 password");
        }
    }
}
