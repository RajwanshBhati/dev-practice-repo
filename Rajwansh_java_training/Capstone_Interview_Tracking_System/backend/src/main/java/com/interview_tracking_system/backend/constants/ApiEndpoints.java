package com.interview_tracking_system.backend.constants;

/**
 * Central place for all API endpoints
 */
public final class ApiEndpoints {

    /**
     * Base path for authentication endpoints
     */
    public static final String BASE_AUTH = "/api/auth";

    /**
     * Login endpoint
     */
    public static final String LOGIN = "/login";

    /**
     * Refresh token endpoint
     */
    public static final String REFRESH = "/refresh";

    /**
     * Logout endpoint
     */
    public static final String LOGOUT = "/logout";

    /**
     * Activate account endpoint
     */
    public static final String ACTIVATE = "/activate";

    private ApiEndpoints() {
        // prevent instantiation
    }
}
