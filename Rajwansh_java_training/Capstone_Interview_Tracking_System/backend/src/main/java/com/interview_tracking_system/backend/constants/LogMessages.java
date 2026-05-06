package com.interview_tracking_system.backend.constants;

/**
 * Centralized log messages for Auth module.
 */
public final class LogMessages {

    /**
     * Private constructor to prevent instantiation.
     */
    private LogMessages() {
    }

    /**
     * Log message for login attempt.
     */
    public static final String LOGIN_ATTEMPT = "Login attempt received for user: {}";

    /**
     * Log message for successful login.
     */
    public static final String LOGIN_SUCCESS = "Login successful for user: {}";

    /**
     * Log message for refresh token request.
     */
    public static final String REFRESH_REQUEST = "Refresh token request received";

    /**
     * Log message for successful token refresh.
     */
    public static final String REFRESH_SUCCESS = "Token refreshed successfully";

    /**
     * Log message for logout request.
     */
    public static final String LOGOUT_REQUEST = "Logout request received for user: {}";

    /**
     * Log message for successful logout.
     */
    public static final String LOGOUT_SUCCESS = "Logout successful for user: {}";

    /**
     * Log message for activation request.
     */
    public static final String ACTIVATION_REQUEST = "Account activation request received";

    /**
     * Log message for successful activation.
     */
    public static final String ACTIVATION_SUCCESS = "Account activated successfully";
}
