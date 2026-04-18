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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@RestController
@RequestMapping("/todos")

public class TodoController{

    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

    public final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }
    
    // Here I Define postmapping to create a new todo item
    @PostMapping
    public ResponseEntity<TodoResponseDTO> createTodo(@Valid @RequestBody TodoRequestDTO todoRequestDTO) {
        logger.info("Received request to create TODO with title: {}", todoRequestDTO.getTitle());

        TodoResponseDTO createdTodo = todoService.createTodo(todoRequestDTO);
        logger.info("TODO created successfully with ID: {}", createdTodo.getId());

        return new ResponseEntity<>(createdTodo, HttpStatus.CREATED);
    }
    

    // for GetMapping to retrieve all todo items and return them as a list of TodoResponseDTOs with an HTTP status of OK.
    @GetMapping
    public ResponseEntity<List<TodoResponseDTO>> getAllTodos() {
        logger.info("Received request to fetch all TODOs");
        List<TodoResponseDTO> todos = todoService.getAllTodos();

        logger.info("Total TODOs returned: {}", todos.size());
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }
    
    // Here I Define GetMapping to retrieve a specific todo item by its ID. It takes the ID as a path variable, calls the getTodoById method of the TodoService, and returns the found todo item as a TodoResponseDTO with an HTTP status of OK.
    @GetMapping("/{id}")
    public ResponseEntity<TodoResponseDTO> getTodoById(@PathVariable Long id) {
        logger.info("Received request to fetch TODO with ID: {}", id);
        TodoResponseDTO todo = todoService.getTodoById(id);
        return new ResponseEntity<>(todo, HttpStatus.OK);
    }
   

   // Here I Define PutMapping to update a specific todo item by its ID.
    @PutMapping("/{id}")
    public ResponseEntity<TodoResponseDTO> updateTodo(@Valid @PathVariable Long id, @RequestBody TodoRequestDTO todoRequestDTO) {
        logger.info("Received request to update TODO with ID: {}", id);
        TodoResponseDTO updatedTodo = todoService.updateTodo(id, todoRequestDTO);
        logger.info("TODO updated successfully with ID: {}", id);
        return new ResponseEntity<>(updatedTodo, HttpStatus.OK);
    }
    

    //Delete the specific todo item by its ID. It takes the ID as a path variable, calls the deleteTodoById method of the TodoService, and returns an HTTP status of NO_CONTENT to indicate that the deletion was successful.
   
   @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodoById(
        @PathVariable Long id,
        @RequestParam(value = "confirmed", defaultValue = "false") boolean confirmed,
        HttpServletRequest request) {
    

     logger.info("Received delete request for TODO ID: {}, confirmed: {}", id, confirmed);
    if (!confirmed) {
        String confirmUrl = request.getRequestURL().toString() + "?confirmed=true";
        logger.warn("Delete not confirmed for TODO ID: {}. Sending confirmation URL", id);

        DeleteConfirmationDTO confirmation = new DeleteConfirmationDTO(
                id,
                "Are you sure you want to delete this todo?",
                confirmUrl
        );

        return ResponseEntity.ok(confirmation);
    }

    todoService.deleteTodoById(id);
    logger.info("TODO deleted successfully with ID: {}", id);
    return ResponseEntity.ok("Todo deleted successfully");
}
}