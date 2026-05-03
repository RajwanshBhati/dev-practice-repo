package com.interview_tracking_system.backend.dto;

import java.time.LocalDateTime;

/**
 * Generic API response wrapper used across all modules.
 *
 * @param <T> type of response data
 */
public class ApiResponse<T> {

    /** Success flag. */
    private boolean success;

    /** Message describing the response. */
    private String message;

    /** Actual response data. */
    private T data;

    /** Timestamp when the response is created. */
    private LocalDateTime timestamp;

    /**
     * Initializes API response with current timestamp.
     */
    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Creates API response with success flag, message, data, and timestamp.
     *
     * @param success response success status
     * @param message response message
     * @param data    response data
     */
    public ApiResponse(final boolean success, final String message, final T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Creates a success API response.
     *
     * @param message response message
     * @param data    response data
     * @param <T>     type of response data
     * @return success API response
     */
    public static <T> ApiResponse<T> success(final String message, final T data) {
        return new ApiResponse<>(true, message, data);
    }

    /**
     * Creates an error API response.
     *
     * @param message error message
     * @param <T>     type of response data
     * @return error API response
     */
    public static <T> ApiResponse<T> error(final String message) {
        return new ApiResponse<>(false, message, null);
    }

    /**
     * Returns success flag.
     *
     * @return success flag
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Returns response message.
     *
     * @return response message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns response data.
     *
     * @return response data
     */
    public T getData() {
        return data;
    }

    /**
     * Returns response timestamp.
     *
     * @return response timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Sets success flag.
     *
     * @param responseSuccess success flag
     */
    public void setSuccess(final boolean responseSuccess) {
        this.success = responseSuccess;
    }

    /**
     * Sets response message.
     *
     * @param responseMessage response message
     */
    public void setMessage(final String responseMessage) {
        this.message = responseMessage;
    }

    /**
     * Sets response data.
     *
     * @param responseData response data
     */
    public void setData(final T responseData) {
        this.data = responseData;
    }

    /**
     * Sets response timestamp.
     *
     * @param responseTimestamp response timestamp
     */
    public void setTimestamp(final LocalDateTime responseTimestamp) {
        this.timestamp = responseTimestamp;
    }
}
