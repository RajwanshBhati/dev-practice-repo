package com.todoapp.springboot.exception;

import com.todoapp.springboot.enums.TodoStatus;
import com.todoapp.springboot.exception.TodoNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


// Here I define the InvalidStatusTransitionException class, which extends RuntimeException. This exception is thrown when an invalid status transition is attempted 
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidStatusTransitionException extends RuntimeException {

    public InvalidStatusTransitionException(TodoStatus from, TodoStatus to) {
        super("Invalid status transition from " + from + " to " + to
                + ". Allowed transitions: PENDING → COMPLETED, COMPLETED → PENDING.");
    }
}
