package com.todoapp.springboot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import com.todoapp.springboot.enums.TodoStatus;


// Here I define the Todo entity class, which represents a todo item in the application. It has fields for id, title, description, status, and createdAt. The class is annotated with @Entity to indicate that it is a JPA entity and @Table to specify the table name in the database.
@Entity
@Table(name = "todos")
public class Todo{
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    // The status field is an enum that represents the current state of the todo item. It is annotated with @Enumerated to specify that it should be stored as a string in the database.
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TodoStatus status;

    @Column(name="created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

   public Todo(){

   }
   

   // Constructor to initialize the title, description, status, and createdAt fields when creating a new todo item. The createdAt field is set to the current date and time when the object is instantiated.
  public Todo(String title, String description, TodoStatus status, LocalDateTime createdAt) {
    this.title = title;
    this.description = description;
    this.status = status;
    this.createdAt = createdAt;
}

   
    // Getters and setters for all the fields in the class, allowing other parts of the application to access and modify the properties of a todo item.
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
    
    @Override
    public String toString() {
        return "Todo [id=" + id + ", title=" + title + ", description=" + description + ", status=" + status + ", createdAt=" + createdAt + "]";
    }

}