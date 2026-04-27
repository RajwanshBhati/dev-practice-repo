package com.interview_tracking_system.backend.dto;

import java.time.LocalDateTime;

/**
 * Generic API response wrapper used across all modules.
 * Wraps data with success flag, message, and timestamp.
 */
public class ApiResponse<T> {

    /** Success flag. */
    private boolean success;
    /** Message describing the response. */
    private String message;
    /** Actual response data. */
    private T data;
    /** Timestamp of when the response is created. */
    private LocalDateTime timestamp;

    /** Default constructor initializes timestamp. */
    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Parameterized constructor to create response with all fields.
     * 
     * @param success
     * @param message
     * @param data
     */
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Factory method for success responses.
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    /**
     * Factory method for error responses.
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }

    /**
     * Getters and setters for all fields.
     * 
     * @return
     */
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
