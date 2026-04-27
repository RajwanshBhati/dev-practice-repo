package com.interview_tracking_system.backend.constants;

public final class CandidateConstants {

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private CandidateConstants() {
        throw new UnsupportedOperationException("Cannot instantiate constants class");
    }

    /**
     * Message returned when candidate registration is successful.
     */
    public static final String MSG_REGISTER_SUCCESS = "Candidate registered successfully.";

    /**
     * Message returned when login is successful.
     */
    public static final String MSG_LOGIN_SUCCESS = "Login successful.";

    /**
     * Message returned when logout is successful.
     */
    public static final String MSG_LOGOUT_SUCCESS = "Logged out successfully.";

    /**
     * Error message when email is already registered.
     */
    public static final String ERROR_EMAIL_EXISTS = "Email already registered.";

    /**
     * Error message when password and confirm password do not match.
     */
    public static final String ERROR_PASSWORD_MISMATCH = "Passwords do not match.";

    /**
     * Error message for invalid login credentials.
     */
    public static final String ERROR_INVALID_CREDENTIALS = "Invalid email or password.";

    /**
     * Error message when candidate has already applied for a job.
     */
    public static final String ERROR_ALREADY_APPLIED = "You have already applied.";

    /**
     * Error message when user is not found in the system.
     */
    public static final String ERROR_NOT_FOUND = "User not found.";

    /**
     * Error message when user is not authenticated or logged in.
     */
    public static final String ERROR_NOT_LOGGED_IN = "User not logged in.";

    /**
     * Error message when no job application is found for the candidate.
     */
    public static final String ERROR_NO_APPLICATION = "No application found.";
}
