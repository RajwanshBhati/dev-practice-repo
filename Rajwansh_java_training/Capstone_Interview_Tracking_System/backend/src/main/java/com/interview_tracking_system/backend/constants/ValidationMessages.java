package com.interview_tracking_system.backend.constants;

/**
 * Utility class that contains all validation message constants
 * used across the application.
 */
public final class ValidationMessages {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ValidationMessages() {
    }

    /** Validation message when refresh token is missing. */
    public static final String REFRESH_TOKEN_REQUIRED = "Refresh token is required";

    /** Validation message when token is missing. */
    public static final String TOKEN_REQUIRED = "Token is required";

    /** Validation message when new password is not provided. */
    public static final String NEW_PASSWORD_REQUIRED = "New password is required";

    /** Validation message when password length is less than required minimum. */
    public static final String PASSWORD_MIN_LENGTH = "Password must be at least 8 characters";

    /** Validation message when confirm password is missing. */
    public static final String CONFIRM_PASSWORD_REQUIRED = "Confirm password is required";

    /** Validation message when job title is missing. */
    public static final String JOB_TITLE_REQUIRED = "Job title is required";

    /** Validation message when job description is missing. */
    public static final String JOB_DESCRIPTION_REQUIRED = "Job description is required";

    /** Validation message when no skills are provided. */
    public static final String SKILLS_REQUIRED = "At least one skill is required";

    /** Validation message when maximum experience is not provided. */
    public static final String MAXEXPERIENCE_REQUIRED = "Max experience is required";

    /** Validation message when minimum experience is not provided. */
    public static final String MINEXPERIENCE_REQUIRED = "Min experience is required";

    /** Validation message when experience value is negative. */
    public static final String EXPERIENCE_NEGATIVE = "Experience cannot be negative";

    /** Validation message when minimum salary is not provided. */
    public static final String MIN_SALARY_REQUIRED = "Minimum salary is required";

    /** Validation message when maximum salary is not provided. */
    public static final String MAX_SALARY_REQUIRED = "Maximum salary is required";

    /** Validation message when salary value is invalid. */
    public static final String SALARY_INVALID = "Salary must be greater than 0";

    /** Validation message when location is missing. */
    public static final String LOCATION_REQUIRED = "Location is required";

    /** Validation message when job type is not provided. */
    public static final String JOB_TYPE_REQUIRED = "Job type is required";
}
