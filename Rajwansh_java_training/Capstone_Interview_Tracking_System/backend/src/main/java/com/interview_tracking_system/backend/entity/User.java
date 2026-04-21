package com.interview_tracking_system.backend.entity;

import com.interview_tracking_system.backend.enums.Role;
import com.interview_tracking_system.backend.enums.Stage;


import jakarta.persistence.*;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_email", columnList = "email")
    })
 public class User {   

 /**
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Full name of the user.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Email of the user used for login, must be unique.
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * Encrypted password of the user.
     *
     * Password should never be stored in plain text.
     * Always store hashed password using BCrypt.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Role of the user (HR / PANEL / CANDIDATE).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /**
     * Indicates whether the user account is active.
     */
    @Column(nullable = false)
    private boolean isActive = true;

    /**
     * Default constructor required by JPA.
     */
    public User() {}

    /**
     * Parameterized constructor for creating a user.
     */
    public User(String name, String email, String password, Role role, boolean isActive) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isActive = isActive;
    }
    /**
     * Getters and setters for all the fields to allow access and modification of the User entity's properties.
     */
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