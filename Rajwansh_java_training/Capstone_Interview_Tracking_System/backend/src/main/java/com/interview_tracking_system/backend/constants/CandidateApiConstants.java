package com.interview_tracking_system.backend.constants;

/**
 * Constants for Candidate API endpoints.
 */
public final class CandidateApiConstants {

    /**
     * Private constructor to prevent instantiation.
     */
    private CandidateApiConstants() {
    }

    /**
     * Base URL for candidate APIs.
     */
    public static final String BASE_URL = "/api/candidates";

    /**
     * Candidate registration endpoint.
     */
    public static final String REGISTER_URL = "/register";

    /**
     * Candidate login endpoint.
     */
    public static final String LOGIN_URL = "/login";

    /**
     * Apply to job endpoint.
     */
    public static final String APPLY_URL = "/apply";

    /**
     * Get candidate status endpoint.
     */
    public static final String STATUS_URL = "/my-status";

    /**
     * Candidate logout endpoint.
     */
    public static final String LOGOUT_URL = "/logout";
}
