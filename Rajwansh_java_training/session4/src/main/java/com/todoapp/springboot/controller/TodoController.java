package com.todoapp.springboot.controller;

import com.todoapp.springboot.dto.TodoRequestDTO;
import com.todoapp.springboot.dto.TodoResponseDTO;
import com.todoapp.springboot.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import com.todoapp.springboot.dto.DeleteConfirmationDTO;
import java.util.List;
import jakarta.validation.Valid;



@RestController
@RequestMapping("/todos")

public class TodoController{

    public final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }
    
    // Here I Define postmapping to create a new todo item
    @PostMapping
    public ResponseEntity<TodoResponseDTO> createTodo(@Valid @RequestBody TodoRequestDTO todoRequestDTO) {
        TodoResponseDTO createdTodo = todoService.createTodo(todoRequestDTO);
        return new ResponseEntity<>(createdTodo, HttpStatus.CREATED);
    }
    

    // for GetMapping to retrieve all todo items and return them as a list of TodoResponseDTOs with an HTTP status of OK.
    @GetMapping
    public ResponseEntity<List<TodoResponseDTO>> getAllTodos() {
        List<TodoResponseDTO> todos = todoService.getAllTodos();
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }
    
    // Here I Define GetMapping to retrieve a specific todo item by its ID. It takes the ID as a path variable, calls the getTodoById method of the TodoService, and returns the found todo item as a TodoResponseDTO with an HTTP status of OK.
    @GetMapping("/{id}")
    public ResponseEntity<TodoResponseDTO> getTodoById(@PathVariable Long id) {
        TodoResponseDTO todo = todoService.getTodoById(id);
        return new ResponseEntity<>(todo, HttpStatus.OK);
    }
   

   // Here I Define PutMapping to update a specific todo item by its ID.
    @PutMapping("/{id}")
    public ResponseEntity<TodoResponseDTO> updateTodo(@Valid @PathVariable Long id, @RequestBody TodoRequestDTO todoRequestDTO) {
        TodoResponseDTO updatedTodo = todoService.updateTodo(id, todoRequestDTO);
        return new ResponseEntity<>(updatedTodo, HttpStatus.OK);
    }
    

    //Delete the specific todo item by its ID. It takes the ID as a path variable, calls the deleteTodoById method of the TodoService, and returns an HTTP status of NO_CONTENT to indicate that the deletion was successful.
   
   @DeleteMapping("/{id}")
public ResponseEntity<?> deleteTodoById(
        @PathVariable Long id,
        @RequestParam(value = "confirmed", defaultValue = "false") boolean confirmed,
        HttpServletRequest request) {

    if (!confirmed) {
        String confirmUrl = request.getRequestURL().toString() + "?confirmed=true";

        DeleteConfirmationDTO confirmation = new DeleteConfirmationDTO(
                id,
                "Are you sure you want to delete this todo?",
                confirmUrl
        );

        return ResponseEntity.ok(confirmation);
    }

    todoService.deleteTodoById(id);
    return ResponseEntity.ok("Todo deleted successfully");
}
}