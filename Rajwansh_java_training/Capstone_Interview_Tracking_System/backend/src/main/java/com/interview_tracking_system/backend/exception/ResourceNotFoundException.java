package com.interview_tracking_system.backend.exception;

/**
 * Thrown when requested resource is not found in database.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
