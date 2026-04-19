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


}