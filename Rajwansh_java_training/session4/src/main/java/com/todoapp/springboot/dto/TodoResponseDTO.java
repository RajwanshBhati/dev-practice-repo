package com.todoapp.springboot.dto;

import com.todoapp.springboot.enums.TodoStatus;
import java.time.LocalDateTime;


// Here I define the TodoResponseDTO class, which is a Data Transfer Object (DTO) used to encapsulate the data for sending a response back to the client when a todo item is created, updated, or retrieved.
public class TodoResponseDTO {

    private Long id;
    private String title;
    private String description;
    private TodoStatus status;
    private LocalDateTime createdAt;

    public TodoResponseDTO() {
    }
    
    public TodoResponseDTO(Long id, String title, String description,TodoStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }
    
    //Getter and Setter Methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
