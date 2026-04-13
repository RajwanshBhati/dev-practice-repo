package com.example.springbootapp.repository;

import com.example.springbootapp.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;



@Repository
public class UserRepository {

    // I used a ConcurrentHashMap to store users in memory, which allows for thread-safe operations. The key is the user ID (Long) and the value is the User object. 
    private final Map<Long, User> userStore = new ConcurrentHashMap<>();

    // I am using an AtomicLong to generate unique IDs for new users. This ensures that even in a concurrent environment, each user gets a unique ID without the risk of race conditions
    private final AtomicLong idSequence = new AtomicLong(1);

    // In the constructor, I am pre-populating the repository with some sample users for testing purposes. 
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
        return Optional.ofNullable(userStore.get(id)); // Nullable is used because get() can return null if the key is not found, and Optional.ofNullable() will handle that case correctly
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