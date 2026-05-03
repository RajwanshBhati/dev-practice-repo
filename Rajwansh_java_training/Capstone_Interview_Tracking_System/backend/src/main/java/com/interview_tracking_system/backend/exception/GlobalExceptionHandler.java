package com.interview_tracking_system.backend.exception;

import com.interview_tracking_system.backend.dto.ApiResponse;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Handles all application exceptions globally.
 */
@RestControllerAdvice
public final class GlobalExceptionHandler {

    /**
     * Handles validation binding exceptions.
     *
     * @param ex bind exception
     * @return error response
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<Void>> handleBindException(final BindException ex) {
        String message = ex.getFieldErrors()
                .stream()
                .map(FieldError::getField)
                .distinct()
                .map(field -> field + " is required or invalid")
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(message));
    }

    /**
     * Handles resource not found exceptions.
     *
     * @param ex resource not found exception
     * @return error response
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(final ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage()));
    }

    /**
     * Handles invalid request exceptions.
     *
     * @param ex invalid request exception
     * @return error response
     */
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(final InvalidRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage()));
    }

    /**
     * Handles generic exceptions.
     *
     * @param ex generic exception
     * @return error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneric(final Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Something went wrong: " + ex.getMessage()));
    }
}
