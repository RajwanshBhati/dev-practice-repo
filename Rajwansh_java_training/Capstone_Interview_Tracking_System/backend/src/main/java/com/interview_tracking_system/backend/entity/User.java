package com.interview_tracking_system.backend.entity;

import com.interview_tracking_system.backend.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * Represents a system user such as HR, PANEL, or CANDIDATE.
 */
@Entity
@Table(
    name = "users",
    indexes = {
        @Index(name = "idx_user_email", columnList = "email")
    }
)
public class User {

    /** Unique identifier for the user. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Full name of the user. */
    @Column(nullable = false)
    private String name;

    /** Unique email used for login. */
    @Column(nullable = false, unique = true)
    private String email;

    /** Encrypted password of the user. */
    @Column(nullable = false)
    private String password;

    /** Role of the user in the system. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /** Whether the user account is active. */
    @Column(nullable = false)
    private boolean active = true;

    /**
     * Default constructor for JPA.
     */
    public User() {
    }

    public User(
            final String userName,
            final String userEmail,
            final String userPassword,
            final Role userRole,
            final boolean activeStatus) {
        this.name = userName;
        this.email = userEmail;
        this.password = userPassword;
        this.role = userRole;
        this.active = activeStatus;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String userName) {
        this.name = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String userEmail) {
        this.email = userEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String userPassword) {
        this.password = userPassword;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(final Role userRole) {
        this.role = userRole;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean activeStatus) {
        this.active = activeStatus;
    }
}