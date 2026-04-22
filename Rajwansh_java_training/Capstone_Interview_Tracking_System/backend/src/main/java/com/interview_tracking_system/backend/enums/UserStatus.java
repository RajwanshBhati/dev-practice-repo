package com.interview_tracking_system.backend.enums;

/**
 * Represents the status of a user account in the system.
 */
public enum UserStatus {
    /** User can log in. */
    ACTIVE,
    /** User cannot log in but account is not deleted. */
    INACTIVE,
    /** User account is locked. */
    LOCKED
}
