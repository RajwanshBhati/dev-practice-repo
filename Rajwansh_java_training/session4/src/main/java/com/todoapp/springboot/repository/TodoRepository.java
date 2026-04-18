package com.todoapp.springboot.repository;
import com.todoapp.springboot.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository// Here I define the TodoRepository interface, which extends JpaRepository. This interface provides CRUD operations for the Todo Entity.
public interface TodoRepository extends JpaRepository<Todo, Long> {
    
}