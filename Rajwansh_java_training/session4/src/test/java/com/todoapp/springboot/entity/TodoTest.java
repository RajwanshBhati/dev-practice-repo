package com.todoapp.springboot.entity;

import com.todoapp.springboot.enums.TodoStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TodoTest {

    // I write a test to verify getters and setters of Todo entity
    @Test
    void testSettersAndGetters() {

        //  I am creating object using default constructor
        Todo todo = new Todo();

        LocalDateTime now = LocalDateTime.now();

        // Setting values using setters
        todo.setId(1L);
        todo.setTitle("Test Title");
        todo.setDescription("Test Description");
        todo.setStatus(TodoStatus.PENDING);


        // I Verifying values using getters
        assertEquals(1L, todo.getId());
        assertEquals("Test Title", todo.getTitle());
        assertEquals("Test Description", todo.getDescription());
        assertEquals(TodoStatus.PENDING, todo.getStatus());
    }

    // Here I write a test to verify all-args constructor of Todo entity
    @Test
    void testAllArgsConstructor() {

        LocalDateTime now = LocalDateTime.now();

        Todo todo = new Todo(
                "Title",
                "Description",
                TodoStatus.COMPLETED,
                now
        );

        assertEquals("Title", todo.getTitle());
        assertEquals("Description", todo.getDescription());
        assertEquals(TodoStatus.COMPLETED, todo.getStatus());
        assertEquals(now, todo.getCreatedAt());
    }

    // I write a test to verify toString method of Todo entity
    @Test
    void testToString() {

        LocalDateTime now = LocalDateTime.now();

        Todo todo = new Todo(
                "Title",
                "Description",
                TodoStatus.PENDING,
                now
        );

        todo.setId(10L);

        String result = todo.toString();

        // Checking if string contains important values
        assertTrue(result.contains("10"));
        assertTrue(result.contains("Title"));
        assertTrue(result.contains("Description"));
        assertTrue(result.contains("PENDING"));
    }
}