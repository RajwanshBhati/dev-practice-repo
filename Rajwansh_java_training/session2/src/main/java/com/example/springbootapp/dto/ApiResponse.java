package com.example.springbootapp.dto;


// I am creating a generic ApiResponse class that can be used to standardize the structure of API responses across the application. This class has two fields: message (a string to convey information about the response) and data (a generic type T to hold any type of data that needs to be returned in the response). The class includes constructors, static factory methods for success and error responses, and getter/setter methods for the fields.
public class ApiResponse<T> {

    private String message;
    private T data;

    public ApiResponse() {
    }
   
    
    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(message, null);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}