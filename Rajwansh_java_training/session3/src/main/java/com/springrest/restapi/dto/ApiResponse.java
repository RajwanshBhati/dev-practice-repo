package com.springrest.restapi.dto;

// Here I am Creating ApiResponse because I want to have a standardized response format for all my API endpoints. This class encapsulates the status of the response (success or error), a message providing additional information about the response, and any relevant data that should be returned to the api
public class ApiResponse<T> {

    private String status;
    private String message;
    private T data;

    public ApiResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
     
    // I am creating static factory methods for success and error responses to simplify the creation of ApiResponse objects. The success method takes a message and data, while the error method only takes a message and sets data to null.
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("SUCCESS", message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("ERROR", message, null);
    }

    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public T getData() { return data; }
}