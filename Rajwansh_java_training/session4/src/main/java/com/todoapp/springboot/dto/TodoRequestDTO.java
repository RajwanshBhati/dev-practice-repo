package com.todoapp.springboot.dto;

import com.todoapp.springboot.enums.TodoStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// Here I define the TodoRequestDTO class, which is a Data Transfer Object (DTO) used to encapsulate the data for creating or updating a todo item. It has fields for title, description, and status, along with validation annotations to ensure that the input data meets certain criteria before being processed by the application.
public class TodoRequestDTO{
    

    //@notnull annotation ensures that the title field cannot be null, and the @Size annotation specifies that the title must be between 1 and 255 characters long. 
    @notNull(message = "Title cannot be null")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @Size(min = 5, message = "Description, if provided, must be at least 5 characters long")
    private String description;
    
    @notNull(message = "Status cannot be null")
    private TodoStatus status;


      public TodoRequestDTO() {
    }
   

   //Created getter and Setter Methods
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TodoStatus getStatus() {
        return status;
    }

    public void setStatus(TodoStatus status) {
        this.status = status;
    }
}