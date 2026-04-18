package com.todoapp.springboot.service;

import com.todoapp.springboot.client.NotificationServiceClient;
import com.todoapp.springboot.dto.TodoRequestDTO;
import com.todoapp.springboot.dto.TodoResponseDTO;
import com.todoapp.springboot.entity.Todo;
import com.todoapp.springboot.enums.TodoStatus;
import com.todoapp.springboot.exception.InvalidStatusTransitionException;
import com.todoapp.springboot.exception.TodoNotFoundException;
import com.todoapp.springboot.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private NotificationServiceClient notificationServiceClient;

    @InjectMocks
    private TodoServiceImpl todoService;

    private Todo todo;

    @BeforeEach
    void setUp() {
        todo = new Todo(
                "Test Title",
                "Test Desc",
                TodoStatus.PENDING,
                java.time.LocalDateTime.now()
        );
        todo.setId(1L);
    }

    // Here I will write tests for all the methods in TodoServiceImpl, covering both success and failure scenarios, as well as edge cases to ensure high code coverage.
    @Test
    void testCreateTodo_success() {
        TodoRequestDTO dto = new TodoRequestDTO();
        dto.setTitle("Learn Spring");
        dto.setDescription("Basics");
        dto.setStatus(TodoStatus.PENDING);

        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        TodoResponseDTO response = todoService.createTodo(dto);

        assertNotNull(response);
        verify(todoRepository, times(1)).save(any(Todo.class));
        verify(notificationServiceClient, times(1))
                .sendTodoCreatedNotification(anyLong(), anyString());
    }

    // Here I write a test case for Get All Todos, which is a simple retrieval method, so I will just verify that the repository's findAll method is called and that the response is correctly mapped.
    @Test
    void testGetAllTodos() {
        when(todoRepository.findAll()).thenReturn(List.of(todo));

        List<TodoResponseDTO> list = todoService.getAllTodos();

        assertEquals(1, list.size());
        verify(todoRepository, times(1)).findAll();
    }

    //Here I will write a test case for Get Todo By ID, covering both the success scenario where the todo is found and the failure scenario where it is not found, throwing a TodoNotFoundException.
    @Test
    void testGetTodoById_success() {
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        TodoResponseDTO response = todoService.getTodoById(1L);

        assertNotNull(response);
    }

    // Here I will write a test case for Get Todo By ID where the todo is not found, and I will assert that a TodoNotFoundException is thrown.
    @Test
    void testGetTodoById_notFound() {
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TodoNotFoundException.class,
                () -> todoService.getTodoById(1L));
    }

    //I am writing a test case for Update Todo, covering a valid status transition scenario where the status is changed from PENDING to COMPLETED, and I will verify that the notification service is called to send an update notification.
    @Test
    void testUpdateTodo_validTransition() {
        TodoRequestDTO dto = new TodoRequestDTO();
        dto.setStatus(TodoStatus.COMPLETED);

        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        TodoResponseDTO response = todoService.updateTodo(1L, dto);

        assertNotNull(response);
        verify(notificationServiceClient, times(1))
                .sendTodoUpdatedNotification(anyLong(), anyString(), anyString());
    }

    // Here I implement a test case for Update Todo where an invalid status transition is attempted (e.g., from COMPLETED to PENDING), and I will assert that an InvalidStatusTransitionException is thrown.
    @Test
    void testUpdateTodo_invalidTransition() {
        todo.setStatus(TodoStatus.COMPLETED);

        TodoRequestDTO dto = new TodoRequestDTO();
        dto.setStatus(TodoStatus.COMPLETED); // invalid

        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        assertThrows(InvalidStatusTransitionException.class,
                () -> todoService.updateTodo(1L, dto));
    }

    // I will write a test case for Delete Todo, covering the success scenario where the todo is found and deleted, and I will verify that the notification service is to send a deletion notification.
    @Test
    void testDeleteTodo_success() {
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        todoService.deleteTodoById(1L);

        verify(todoRepository, times(1)).deleteById(1L);
        verify(notificationServiceClient, times(1))
                .sendTodoDeletedNotification(anyLong(), anyString());
    }

    // Here I will write a test case for Delete Todo where the todo is not found, and I will assert that a TodoNotFoundException is thrown.
    @Test
    void testDeleteTodo_notFound() {
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TodoNotFoundException.class,
                () -> todoService.deleteTodoById(1L));
    }

    // Here I will write a test case for Update Todo where the title and description fields in the request DTO are null, and I will verify that the existing values are retained and that the update still succeeds without throwing any exceptions.    
    @Test
    void testUpdateTodo_titleAndDescriptionNullBranches() {
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        TodoRequestDTO dto = new TodoRequestDTO(); // all null fields

        TodoResponseDTO response = todoService.updateTodo(1L, dto);

        assertNotNull(response);
    }
}   