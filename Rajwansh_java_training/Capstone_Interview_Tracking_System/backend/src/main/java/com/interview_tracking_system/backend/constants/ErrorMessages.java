package com.interview_tracking_system.backend.constants;

/**
 * Utility class that contains all centralized error message constants
 * used across the application.
 */
public final class ErrorMessages {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ErrorMessages() {
    }

    /** Error message when a user is not found in the system. */
    public static final String USER_NOT_FOUND = "User not found";

    /** Error message when a user account is not active. */
    public static final String ACCOUNT_NOT_ACTIVE = "Account is not active";

    /** Error message for invalid refresh token. */
    public static final String INVALID_REFRESH_TOKEN = "Invalid refresh token";

    /** Error message when refresh token has expired. */
    public static final String REFRESH_TOKEN_EXPIRED = "Refresh token expired";

    /** Error message for invalid account activation token. */
    public static final String INVALID_ACTIVATION_TOKEN = "Invalid token";

    /** Error message when activation token has expired. */
    public static final String ACTIVATION_TOKEN_EXPIRED = "Token expired";

    /** Error message when password and confirm password do not match. */
    public static final String PASSWORD_MISMATCH = "Passwords do not match";
}
