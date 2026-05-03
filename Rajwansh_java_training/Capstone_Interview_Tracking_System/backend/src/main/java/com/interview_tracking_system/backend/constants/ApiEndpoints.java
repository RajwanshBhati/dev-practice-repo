package com.interview_tracking_system.backend.constants;

/**
 * Central place for all API endpoints.
 */
public final class ApiEndpoints {

    /**
     * Base path for all API endpoints.
     */
    public static final String BASE_API = "/api";

    /**
     * Base path for authentication endpoints.
     */
    public static final String BASE_AUTH = "/api/auth";

    /**
     * Login endpoint.
     */
    public static final String LOGIN = "/login";

    /**
     * Refresh token endpoint.
     */
    public static final String REFRESH = "/refresh";

    /**
     * Logout endpoint.
     */
    public static final String LOGOUT = "/logout";

    /**
     * Activate account endpoint.
     */
    public static final String ACTIVATE = "/activate";

    /**
     * Base path for HR job description endpoints.
     */
    public static final String HR_JD = "/hr/jd";

    /**
     * Job description endpoint.
     */
    public static final String JD = "/jd";

    /**
     * Search endpoint for job descriptions.
     */
    public static final String JD_SEARCH = HR_JD + "/search";

    /**
     * Base path for versioned API endpoints.
     */
    public static final String BASE = "/api/v1";

    /**
     * Resume file endpoint.
     */
    public static final String RESUME_FILE = "/{fileName:.+}";

    /**
     * Panel endpoints.
     */
    public static final String PANEL = BASE + "/panel";

    /**
     * Create panel endpoints.
     */
    public static final String CREATE = "/create";

    /**
     * Show list endpoints.
     */
    public static final String LIST = "/list";

    /**
     * Interviewer endpoints.
     */
    public static final String GETPANEL = "/interviews";

    /**
     * HR endpoints.
     */
    public static final String SHOWHR = "/api/hr";

    /**
     * Candidate endpoints.
     */
    public static final String SHOWCANDIDATE = "/candidates";

    /**
     * Feedback endpoints.
     */
    public static final String SHOWFEED = "/feedback";

    /**
     * Resume endpoints.
     */
    public static final String RESUMES = BASE_API + "/resumes";

    /**
     * Interview base endpoints.
     */
    public static final String INTERVIEW = BASE_API + "/interview";

    /**
     * Schedule interview endpoint.
     */
    public static final String INTERVIEW_SCHEDULE = "/schedule";

    /**
     * Update candidate status endpoint.
     */
    public static final String INTERVIEW_STATUS = "/status";

    /**
     * Submit feedback endpoint.
     */
    public static final String INTERVIEW_FEEDBACK = "/feedback";

    /**
     * Panel interview fetch endpoint.
     */
    public static final String INTERVIEW_PANEL = "/panel";

    /**
     * Candidate interview fetch endpoint.
     */
    public static final String INTERVIEW_CANDIDATE = "/candidate";

    private ApiEndpoints() {

    }
}
