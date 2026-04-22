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
@Table(name = "users", indexes = {
        @Index(name = "idx_user_email", columnList = "email")
})
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
     * Default constructor for creating a user instance.
     */
    public User() {

    }

    /**
     * Parameterized constructor for creating a user with all details.
     *
     * @param userName
     * @param userEmail
     * @param userPassword
     * @param userRole
     * @param activeStatus
     */
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

    /**
     * Returns the unique identifier for the user.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the name of the user.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     *
     * @param userName the name to set
     */
    public void setName(final String userName) {
        this.name = userName;
    }

    /**
     * Returns the email of the user.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user.
     *
     * @param userEmail the email to set
     */
    public void setEmail(final String userEmail) {
        this.email = userEmail;
    }

    /**
     * Returns the password of the user.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param userPassword the password to set
     */
    public void setPassword(final String userPassword) {
        this.password = userPassword;
    }

    /**
     * Returns the role of the user.
     *
     * @return the role
     */
    public Role getRole() {
        return role;
    }

    /**
     * Sets the role of the user.
     *
     * @param userRole the role to set
     */
    public void setRole(final Role userRole) {
        this.role = userRole;
    }

    /**
     * Returns whether the user account is active.
     *
     * @return true if the user is active, false otherwise
     */

    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active status of the user account.
     *
     * @param activeStatus the active status to set
     */
    public void setActive(final boolean activeStatus) {
        this.active = activeStatus;
    }
}
