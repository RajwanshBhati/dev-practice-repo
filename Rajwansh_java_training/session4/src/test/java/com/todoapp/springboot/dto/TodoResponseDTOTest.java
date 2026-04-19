package com.todoapp.springboot.dto;

import com.todoapp.springboot.enums.TodoStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TodoResponseDTOTest {

    // I write a test to verify getters and setters of TodoResponseDTO
    @Test
    void testSettersAndGetters() {

        // I am creating object using default constructor
        TodoResponseDTO dto = new TodoResponseDTO();

        LocalDateTime now = LocalDateTime.now();

        // Setting values using setters
        dto.setId(1L);
        dto.setTitle("Test Title");
        dto.setDescription("Test Description");
        dto.setStatus(TodoStatus.PENDING);
        dto.setCreatedAt(now);

        // Verifying values using getters
        assertEquals(1L, dto.getId());
        assertEquals("Test Title", dto.getTitle());
        assertEquals("Test Description", dto.getDescription());
        assertEquals(TodoStatus.PENDING, dto.getStatus());
        assertEquals(now, dto.getCreatedAt());
    }

    // Here I write a test to verify all-args constructor of TodoResponseDTO
    @Test
    void testAllArgsConstructor() {

        LocalDateTime now = LocalDateTime.now();

        // Creating object using all-args constructor
        TodoResponseDTO dto = new TodoResponseDTO(
                2L,
                "Title",
                "Description",
                TodoStatus.COMPLETED,
                now
        );

        // Verifying all fields using getters
        assertEquals(2L, dto.getId());
        assertEquals("Title", dto.getTitle());
        assertEquals("Description", dto.getDescription());
        assertEquals(TodoStatus.COMPLETED, dto.getStatus());
        assertEquals(now, dto.getCreatedAt());
    }
}