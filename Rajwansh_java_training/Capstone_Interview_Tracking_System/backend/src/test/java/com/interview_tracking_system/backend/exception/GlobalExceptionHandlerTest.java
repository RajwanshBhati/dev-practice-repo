package com.interview_tracking_system.backend.exception;

/**
 * Static imports for assertion methods.
 */
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JUnit import for test methods.
 */
import org.junit.jupiter.api.Test;

/**
 * Spring imports for HTTP status and validation handling.
 */
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

/**
 * This class tests GlobalExceptionHandler.
 *
 * It verifies that different exceptions return correct HTTP status
 * and validation errors are handled properly.
 */
class GlobalExceptionHandlerTest {

        /**
         * Tests all exception handler methods for correct status codes and messages.
         */
        @Test
        void handlersShouldReturnExpectedStatusCodes() {

                GlobalExceptionHandler handler = new GlobalExceptionHandler();

                assertEquals(
                                HttpStatus.NOT_FOUND,
                                handler.handleNotFound(new ResourceNotFoundException("missing"))
                                                .getStatusCode());

                assertEquals(
                                HttpStatus.BAD_REQUEST,
                                handler.handleBadRequest(new InvalidRequestException("bad"))
                                                .getStatusCode());

                assertEquals(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                handler.handleGeneric(new RuntimeException("boom"))
                                                .getStatusCode());

                BindException bindException = new BindException(new Object(), "request");

                bindException.addError(
                                new FieldError("request", "email", "Email required"));

                var response = handler.handleBindException(bindException);

                assertEquals(
                                HttpStatus.BAD_REQUEST,
                                response.getStatusCode());

                assertTrue(
                                response.getBody().getMessage().contains("email"));
        }
}
