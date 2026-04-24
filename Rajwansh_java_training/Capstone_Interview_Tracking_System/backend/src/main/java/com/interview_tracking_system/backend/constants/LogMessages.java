package com.interview_tracking_system.backend.constants;

/**
 * Centralized log messages for Auth module
 */
public final class LogMessages {

    private LogMessages() {

    }

    /**
     * Log messages for authentication operations
     */

    public static final String LOGIN_ATTEMPT = "Login attempt received for user: {}";

    public static final String LOGIN_SUCCESS = "Login successful for user: {}";

    public static final String REFRESH_REQUEST = "Refresh token request received";

    public static final String REFRESH_SUCCESS = "Token refreshed successfully";

    public static final String LOGOUT_REQUEST = "Logout request received for user: {}";

    public static final String LOGOUT_SUCCESS = "Logout successful for user: {}";

    public static final String ACTIVATION_REQUEST = "Account activation request received";

    public static final String ACTIVATION_SUCCESS = "Account activated successfully";
}
