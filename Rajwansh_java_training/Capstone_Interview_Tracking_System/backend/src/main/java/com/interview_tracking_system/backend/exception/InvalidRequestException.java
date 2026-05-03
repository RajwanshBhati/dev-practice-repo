package com.interview_tracking_system.backend.exception;

/**
 * Thrown when request data is invalid or business rule fails.
 */
public final class InvalidRequestException extends RuntimeException {

    /**
     * Constructs exception with message.
     *
     * @param message error message
     */
    public InvalidRequestException(final String message) {
        super(message);
    }
}
