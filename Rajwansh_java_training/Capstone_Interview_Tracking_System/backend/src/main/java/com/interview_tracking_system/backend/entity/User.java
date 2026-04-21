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
    private boolean isActive = true;

    /**
     * Default constructor for JPA.
     */
    public User() {
    }

    /**
     * Parameterized constructor for creating a user.
     *
     * @param userName     the user's full name
     * @param userEmail    the user's email address
     * @param userPassword the user's encrypted password
     * @param userRole     the user's role in the system
     * @param activeStatus whether the account is active
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
        this.isActive = activeStatus;
    }

    /**
     * Returns the user id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the user's name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the user's name.
     *
     * @param userName the name to set
     */
    public void setName(final String userName) {
        this.name = userName;
    }

    /**
     * Returns the user's email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email.
     *
     * @param userEmail the email to set
     */
    public void setEmail(final String userEmail) {
        this.email = userEmail;
    }

    /**
     * Returns the user's password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     *
     * @param userPassword the password to set
     */
    public void setPassword(final String userPassword) {
        this.password = userPassword;
    }

    /**
     * Returns the user's role.
     *
     * @return the role
     */
    public Role getRole() {
        return role;
    }

    /**
     * Sets the user's role.
     *
     * @param userRole the role to set
     */
    public void setRole(final Role userRole) {
        this.role = userRole;
    }

    /**
     * Returns whether the user account is active.
     *
     * @return true if active
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets whether the user account is active.
     *
     * @param activeStatus the active status to set
     */
    public void setActive(final boolean activeStatus) {
        this.isActive = activeStatus;
    }
}