package com.todoapp.springboot.service;

import com.todoapp.springboot.dto.TodoRequestDTO;
import com.todoapp.springboot.dto.TodoResponseDTO;
import java.util.List;
import com.todoapp.springboot.dto.DeleteConfirmationDTO;


public interface TodoService {
    
    // Here I define the TodoService interface, which declares the methods for creating, retrieving, updating, and deleting todo items. Each method takes appropriate parameters and returns a TodoResponseDTO 
    TodoResponseDTO createTodo(TodoRequestDTO todoRequestDTO);
    List<TodoResponseDTO> getAllTodos();

    TodoResponseDTO getTodoById(Long id);
    TodoResponseDTO updateTodo(Long id, TodoRequestDTO todoRequestDTO);
    DeleteConfirmationDTO getDeleteConfirmation(Long id, String baseUrl);
    void deleteTodoById(Long id);

}