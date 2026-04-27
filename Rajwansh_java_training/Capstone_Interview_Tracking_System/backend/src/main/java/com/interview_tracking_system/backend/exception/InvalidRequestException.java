package com.interview_tracking_system.backend.exception;

/**
 * Thrown when request data is invalid or business rule fails.
 */
public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(String message) {
        super(message);
    }
}
