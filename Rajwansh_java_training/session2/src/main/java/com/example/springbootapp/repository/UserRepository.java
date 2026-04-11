package com.training.springbootapp.repository;

import com.training.springbootapp.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository {

    // I used Thread-safe storage for users handles multiple requests safely
    private final Map<Long, User> userStore = new ConcurrentHashMap<>();

    // I used AtomicLong to generate unique IDs for new users
    private final AtomicLong idSequence = new AtomicLong(1);

    // Constructor to initialize with some sample users
    public UserRepository() {
        save(new User(null, "Ajay", "ajay@gmail.com", "ADMIN"));
        save(new User(null, "Rishu", "rishu@gmail.com", "USER"));
    }

    // Fetch all users
    public List<User> findAll() {
        return new ArrayList<>(userStore.values());
    }

    // Find single user by ID
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userStore.get(id));
    }

    // Save or update user
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idSequence.getAndIncrement());
        }
        userStore.put(user.getId(), user);
        return user;
    }

    
    public void deleteById(Long id) {
        userStore.remove(id);
    }

    
    public boolean existsById(Long id) {
        return userStore.containsKey(id);
    }
}