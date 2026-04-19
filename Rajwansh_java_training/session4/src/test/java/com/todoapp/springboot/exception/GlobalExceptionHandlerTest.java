package com.todoapp.springboot.exception;

import com.todoapp.springboot.enums.TodoStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    // This method runs before every test and initializes the handler
    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    // Test for TodoNotFoundException → should return 404
    @Test
    void handleTodoNotFound_shouldReturnNotFound() {

        // I am creating a custom exception with a message
        TodoNotFoundException ex = new TodoNotFoundException(1L);

        // Calling the handler method
        ResponseEntity<Map<String, Object>> response = handler.handleTodoNotFound(ex);

        // Verifying status code
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Verifying response body
        assertEquals("Todo not found with ID: 1", response.getBody().get("message"));
        assertNotNull(response.getBody().get("time"));
    }

    // Test for InvalidStatusTransitionException → should return 400
    @Test
    void handleInvalidStatus_shouldReturnBadRequest() {

        // I am creating a custom exception with from and to status
        InvalidStatusTransitionException ex =
                new InvalidStatusTransitionException(TodoStatus.PENDING, TodoStatus.COMPLETED);

        ResponseEntity<Map<String, Object>> response = handler.handleInvalidStatus(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody().get("message"));
    }

    // I write a test for validation exception → should return 400 with field errors
    @Test
    void handleValidationException_shouldReturnFieldErrors() {

        // I am creating a dummy object to attach validation errors
        Object target = new Object();

        // Binding result to simulate validation failure
        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(target, "object");

        // Adding a fake field error for "title" field
        bindingResult.addError(new FieldError("object", "title", "Title is required"));

        // Creating exception with binding result to simulate validation failure
        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<Map<String, Object>> response =
                handler.handleValidationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Map<String, String> errors =
                (Map<String, String>) response.getBody().get("errors");

        assertEquals("Title is required", errors.get("title"));
    }

    //  I write a test for generic exception → should return 500
    @Test
    void handleGeneric_shouldReturnInternalServerError() {

        Exception ex = new Exception("Something broke");

        ResponseEntity<Map<String, Object>> response =
                handler.handleGeneric(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        assertTrue(response.getBody().get("message")
                .toString().contains("Something broke"));
    }
}