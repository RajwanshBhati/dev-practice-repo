package com.interview_tracking_system.backend.constants;

/**
 * Central place for validation messages.
 */
public final class ValidationMessages {

    private ValidationMessages() {
        // prevent instantiation
    }

    /**
     * Authentication validation messages
     */
    public static final String REFRESH_TOKEN_REQUIRED = "Refresh token is required";

    public static final String TOKEN_REQUIRED = "Token is required";

    public static final String NEW_PASSWORD_REQUIRED = "New password is required";

    public static final String PASSWORD_MIN_LENGTH = "Password must be at least 8 characters";

    public static final String CONFIRM_PASSWORD_REQUIRED = "Confirm password is required";

    /**
     * Job Description validation messages
     *
     */
    public static final String JOB_TITLE_REQUIRED = "Job title is required";

    public static final String JOB_DESCRIPTION_REQUIRED = "Job description is required";

    public static final String SKILLS_REQUIRED = "At least one skill is required";

    public static final String MAXEXPERIENCE_REQUIRED = "Max experience is required";

    public static final String MINEXPERIENCE_REQUIRED = "Min experience is required";

    public static final String EXPERIENCE_NEGATIVE = "Experience cannot be negative";

    public static final String MIN_SALARY_REQUIRED = "Minimum salary is required";

    public static final String MAX_SALARY_REQUIRED = "Maximum salary is required";

    public static final String SALARY_INVALID = "Salary must be greater than 0";

    public static final String LOCATION_REQUIRED = "Location is required";

    public static final String JOB_TYPE_REQUIRED = "Job type is required";

}
