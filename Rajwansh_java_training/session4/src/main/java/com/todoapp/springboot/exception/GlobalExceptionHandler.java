package com.todoapp.springboot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.todoapp.springboot.exception.TodoNotFoundException;
import com.todoapp.springboot.exception.InvalidStatusTransitionException;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

// Here I define the GlobalExceptionHandler class, which is annotated with @RestControllerAdvice. This class contains methods to handle specific exceptions (TodoNotFoundException and InvalidStatusTransitionException) as well as a generic handler for any other exceptions that may occur.
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TodoNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleTodoNotFound(TodoNotFoundException ex) {
        return createResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(InvalidStatusTransitionException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidStatus(InvalidStatusTransitionException ex) {
        return createResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return createResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    // Common method to create a standardized response structure for all exceptions, including the timestamp, HTTP status code, and error message.
    private ResponseEntity<Map<String, Object>> createResponse(HttpStatus status, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("time", LocalDateTime.now());
        response.put("status", status.value());
        response.put("message", message);

        return new ResponseEntity<>(response, status);
    }
}