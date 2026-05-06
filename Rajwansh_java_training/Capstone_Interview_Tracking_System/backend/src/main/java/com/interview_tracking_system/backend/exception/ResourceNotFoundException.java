package com.interview_tracking_system.backend.exception;

/**
 * Thrown when requested resource is not found in database.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs exception with message.
     *
     * @param message error message
     */
    public ResourceNotFoundException(final String message) {
        super(message);
    }
}
