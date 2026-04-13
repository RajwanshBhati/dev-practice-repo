package com.example.springbootapp.controller;


import com.example.springbootapp.model.User;
import com.example.springbootapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.springbootapp.dto.CreateUserRequest;
import com.example.springbootapp.dto.ApiResponse;

import java.util.List;


@RestController
@RequestMapping("/users") // I used the @RestController annotation to indicate that this class is a RESTful controller, and the @RequestMapping annotation to specify that all endpoints in this controller will be prefixed with "/users". This means that any HTTP requests to paths starting with "/users" will be handled by methods in this controller.
public class UserController {

    private final UserService userService;

    
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // I implemented the getAllUsers method to retrieve all users from the repository. This method simply calls the findAll method of the UserRepository and returns the list of users.
    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();

        return ResponseEntity.ok(
                ApiResponse.success("Users fetched successfully", users)
        );
    }

    // I implemented the getUserById method to fetch a single user by their ID. If the user is not found, it throws a ResourceNotFoundException, which is a custom exception 
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);

        return ResponseEntity.ok(
                ApiResponse.success("User fetched successfully", user)
        );
    }

    
    //  I created the createUser method to create a new user with validation
    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(
            @Valid @RequestBody CreateUserRequest request) {

        User createdUser = userService.createUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("User created successfully", createdUser));
    }

    // Update existing user 
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody CreateUserRequest request) {

        User updatedUser = userService.updateUser(id, request);

        return ResponseEntity.ok(
                ApiResponse.success("User updated successfully", updatedUser)
        );
    }

    // Delete user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.ok(
                ApiResponse.success("User deleted successfully", null)
        );
    }
}