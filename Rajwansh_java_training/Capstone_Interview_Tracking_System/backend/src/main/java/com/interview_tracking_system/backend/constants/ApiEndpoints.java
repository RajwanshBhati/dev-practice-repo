package com.interview_tracking_system.backend.constants;

/**
 * Central place for all API endpoints
 */
public final class ApiEndpoints {

    /**
     * Base path for all API endpoints
     */

    public static final String BASE_API = "/api";

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

    public static final String HR_JD = "/hr/jd";

    /**
     * Base path for all API endpoints
     *
     */

    public static final String JD = "/jd";

    /**
     * Search endpoint for job descriptions
     */
    public static final String JD_SEARCH = HR_JD + "/search";

    public static final String BASE = "/api/v1";

    public static final String PANEL = BASE + "/panel";
    public static final String CREATE = "/create";
    public static final String LIST = "/list";

    public static final String GETPANEL = "/interviews";

    private ApiEndpoints() {

    }
}
