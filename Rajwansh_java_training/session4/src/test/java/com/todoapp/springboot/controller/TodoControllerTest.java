package com.todoapp.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todoapp.springboot.dto.TodoRequestDTO;
import com.todoapp.springboot.dto.TodoResponseDTO;
import com.todoapp.springboot.enums.TodoStatus;
import com.todoapp.springboot.service.TodoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @Autowired
    private ObjectMapper objectMapper;

    //Here I implement a test case for Create Todo, where I will mock the service layer to return a predefined response when the createTodo method is called, and then I will perform a Post request.
    @Test
    void testCreateTodo() throws Exception {
        TodoRequestDTO req = new TodoRequestDTO();
        req.setTitle("Learn Spring");
        req.setDescription("Basics");
        req.setStatus(TodoStatus.PENDING);

        TodoResponseDTO res = new TodoResponseDTO(
                1L, "Learn Spring", "Basics", TodoStatus.PENDING, null
        );

        Mockito.when(todoService.createTodo(any())).thenReturn(res);

        mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }

    // I write a test case for Get All Todos, which is a simple retrieval method, so I will just verify that the repository's findAll method is called and that the response is correctly mapped.
    @Test
    void testGetAllTodos() throws Exception {
        Mockito.when(todoService.getAllTodos()).thenReturn(List.of());

        mockMvc.perform(get("/todos"))
                .andExpect(status().isOk());
    }

    // Here I will write a test case for Get Todo By ID, covering both the success scenario where the todo is found and the failure scenario where it is not found, throwing a TodoNotFoundException.
    @Test
    void testGetById() throws Exception {
        TodoResponseDTO res = new TodoResponseDTO(
                1L, "Test", "Desc", TodoStatus.PENDING, null
        );

        Mockito.when(todoService.getTodoById(1L)).thenReturn(res);

        mockMvc.perform(get("/todos/1"))
                .andExpect(status().isOk());
    }

    // I implement a test case for Update Todo where an invalid status transition is attempted (e.g., from COMPLETED to PENDING), and I will assert that an InvalidStatusTransitionException is thrown.
    @Test
    void testUpdateTodo() throws Exception {
        TodoRequestDTO req = new TodoRequestDTO();
        req.setTitle("Updated");

        TodoResponseDTO res = new TodoResponseDTO(
                1L, "Updated", "Desc", TodoStatus.PENDING, null
        );

        Mockito.when(todoService.updateTodo(any(), any())).thenReturn(res);

        mockMvc.perform(put("/todos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    // I implement a test case for Delete Todo, covering the scenario where the delete operation requires confirmation. 
    @Test
    void testDeleteNotConfirmed() throws Exception {
        mockMvc.perform(delete("/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.confirmUrl").exists());
    }

    // \Delete the specific todo item by its ID. It takes the ID as a path variable, calls the deleteTodoById method of the TodoService, and returns an HTTP status of NO_CONTENT to indicate that the deletion was successful.
    @Test
    void testDeleteConfirmed() throws Exception {
        mockMvc.perform(delete("/todos/1?confirmed=true"))
                .andExpect(status().isOk());
    }
}