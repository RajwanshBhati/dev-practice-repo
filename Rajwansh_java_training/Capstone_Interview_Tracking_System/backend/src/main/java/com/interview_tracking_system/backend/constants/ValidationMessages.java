package com.interview_tracking_system.backend.constants;

/**
 * Central place for validation messages.
 */
public final class ValidationMessages {

    private ValidationMessages() {
        // prevent instantiation
    }

    public static final String REFRESH_TOKEN_REQUIRED = "Refresh token is required";

    public static final String TOKEN_REQUIRED = "Token is required";

    public static final String NEW_PASSWORD_REQUIRED = "New password is required";

    public static final String PASSWORD_MIN_LENGTH = "Password must be at least 8 characters";

    public static final String CONFIRM_PASSWORD_REQUIRED = "Confirm password is required";
}
