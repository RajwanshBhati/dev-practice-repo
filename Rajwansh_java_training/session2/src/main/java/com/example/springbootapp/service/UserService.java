package com.example.springbootapp.service;


import com.example.springbootapp.exception.ResourceNotFoundException;
import com.example.springbootapp.model.User;
import com.example.springbootapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {

    private final UserRepository userRepository;

    //Here I am using Constructor Injection 
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /// Get user by ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    // I added validation to ensure data integrity and prevent invalid user creation
    public User createUser(CreateUserRequest request) {

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (!request.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        String role = (request.getRole() == null || request.getRole().isEmpty())
                ? "USER"
                : request.getRole().toUpperCase().trim();

        User newUser = new User(
                null,
                request.getName().trim(),
                request.getEmail().toLowerCase().trim(),
                role
        );

        return userRepository.save(newUser);
    }

    // I Implemented update logic to allow partial updates 
    public User updateUser(Long id, CreateUserRequest request) {

        // Check if user exists
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

        // Update fields with validation
        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            existingUser.setName(request.getName().trim());
        }

        if (request.getEmail() != null && request.getEmail().contains("@")) {
            existingUser.setEmail(request.getEmail().toLowerCase().trim());
        }

        if (request.getRole() != null && !request.getRole().isEmpty()) {
            existingUser.setRole(request.getRole().toUpperCase().trim());
        }

        // Save updated user
        return userRepository.save(existingUser);
    }

    
    // I added delete logic to remove users by ID and handle non-existent users gracefully
    public void deleteUser(Long id) {

        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", id);
        }

        userRepository.deleteById(id);
    }
}