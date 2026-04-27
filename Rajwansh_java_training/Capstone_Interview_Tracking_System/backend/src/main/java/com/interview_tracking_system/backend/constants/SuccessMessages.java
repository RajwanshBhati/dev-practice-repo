package com.interview_tracking_system.backend.constants;

/**
 * Centralized success messages for Auth module
 */
public final class SuccessMessages {

    private SuccessMessages() {
        // prevent instantiation
    }

    public static final String LOGIN_SUCCESS = "Login successful";

    public static final String TOKEN_REFRESHED = "Token refreshed";

    public static final String LOGOUT_SUCCESS = "Logged out successfully";

    public static final String ACCOUNT_ACTIVATED = "Account activated successfully. You can now login.";
}
