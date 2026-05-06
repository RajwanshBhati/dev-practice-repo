package com.interview_tracking_system.backend.constants;

/**
 * Centralized success messages for Auth module.
 */
public final class SuccessMessages {

    /**
     * Private constructor to prevent instantiation.
     */
    private SuccessMessages() {

    }

    /**
     * Message for successful login.
     */
    public static final String LOGIN_SUCCESS = "Login successful";

    /**
     * Message for successful token refresh.
     */
    public static final String TOKEN_REFRESHED = "Token refreshed";

    /**
     * Message for successful logout.
     */
    public static final String LOGOUT_SUCCESS = "Logged out successfully";

    /**
     * Message for successful account activation.
     */
    public static final String ACCOUNT_ACTIVATED = "Account activated successfully. You can now login.";
}
