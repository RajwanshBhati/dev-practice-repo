package com.todoapp.springboot.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


// Here I define the TodoNotFoundException class, which extends RuntimeException. This exception is thrown when a requested todo item is not found in the database. 
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TodoNotFoundException extends RuntimeException {
    public TodoNotFoundException(Long id) {
        super("Todo not found with ID: " + id);
    }
}