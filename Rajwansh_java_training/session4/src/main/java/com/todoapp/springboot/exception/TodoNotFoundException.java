package com.todoapp.springboot.exception;

public class TodoNotFoundException extends RuntimeException {
    public TodoNotFoundException(Long id) {
        super("Todo not found with ID: " + id);
    }
}