package com.interview_tracking_system.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")

// Here I defined the User entity with fields like id, name, email, password, role, and isActive.
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean isActive;

    // Constructors for User entity
    public User() {}

    public User(String name, String email, String password, Role role, boolean isActive) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isActive = isActive;
    }

    // Here I defined getters and setters for all the fields to allow access and modification of the User entity's properties.
    public Long getId() { 
        return id; 
    }

    public String getName() { 
        return name; 
    }

    public void setName(String name) { 
        this.name = name; 
    }

    public String getEmail() { 
        return email; 
    }

    public void setEmail(String email) { 
        this.email = email; 
    }

    public String getPassword() { 
        return password; 
    }

    public void setPassword(String password) { 
        this.password = password; 
    }

    public Role getRole() { 
        return role; 
    }

    public void setRole(Role role) { 
        this.role = role; 
    }

    public boolean isActive() { 
        return isActive; 
    }

    public void setActive(boolean active) { 
        isActive = active; 
    }
    
}