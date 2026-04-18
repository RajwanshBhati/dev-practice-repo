package com.todo.app.exception;

import com.todo.app.enums.TodoStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidStatusTransitionException extends RuntimeException {

    public InvalidStatusTransitionException(TodoStatus from, TodoStatus to) {
        super("Invalid status transition from " + from + " to " + to
                + ". Allowed transitions: PENDING → COMPLETED, COMPLETED → PENDING.");
    }
}
