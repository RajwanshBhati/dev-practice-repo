package com.interview_tracking_system.backend.entity;

import com.interview_tracking_system.backend.enums.Role;
import com.interview_tracking_system.backend.enums.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

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

    /** Unique mobile number of the user. */
    @Column(unique = true)
    private String mobile;

    /** Encrypted password of the user. */
    @Column(nullable = false)
    private String password;

    /** Role of the user in the system. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /** Status of the user account (ACTIVE, INACTIVE, etc.). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    /** Organisation the user belongs to. */
    @Column
    private String organisation;

    /** Designation of the user (mainly for panel members). */
    @Column
    private String designation;

    /** Activation token for first-time login or password setup. */
    @Column(name = "activation_token")
    private String activationToken;

    /** Expiry time for the activation token. */
    @Column(name = "activation_token_expiry")
    private LocalDateTime activationTokenExpiry;

    /** Timestamp when the user was created. */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /** Timestamp when the user was last updated. */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Default constructor for creating a user instance.
     */
    public User() {
    }

    /**
     * Parameterized constructor for creating a user with all details.
     *
     * @param userName     the user's name
     * @param userEmail    the user's email
     * @param userPassword the user's password
     * @param userRole     the user's role
     * @param userStatus   the user's status
     */
    public User(
            final String userName,
            final String userEmail,
            final String userPassword,
            final Role userRole,
            final UserStatus userStatus) {
        this.name = userName;
        this.email = userEmail;
        this.password = userPassword;
        this.role = userRole;
        this.status = userStatus;
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
     * Returns the mobile number of the user.
     *
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Sets the mobile number of the user.
     *
     * @param userMobile the mobile number to set
     */
    public void setMobile(final String userMobile) {
        this.mobile = userMobile;
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
     * Returns the status of the user.
     *
     * @return the status
     */
    public UserStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of the user.
     *
     * @param userStatus the status to set
     */
    public void setStatus(final UserStatus userStatus) {
        this.status = userStatus;
    }

    /**
     * Returns the organisation of the user.
     *
     * @return the organisation
     */
    public String getOrganisation() {
        return organisation;
    }

    /**
     * Sets the organisation of the user.
     *
     * @param org the organisation to set
     */
    public void setOrganisation(final String org) {
        this.organisation = org;
    }

    /**
     * Returns the designation of the user.
     *
     * @return the designation
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * Sets the designation of the user.
     *
     * @param userDesignation the designation to set
     */
    public void setDesignation(final String userDesignation) {
        this.designation = userDesignation;
    }

    /**
     * Returns the activation token.
     *
     * @return the activation token
     */
    public String getActivationToken() {
        return activationToken;
    }

    /**
     * Sets the activation token.
     *
     * @param token the token to set
     */
    public void setActivationToken(final String token) {
        this.activationToken = token;
    }

    /**
     * Returns the activation token expiry.
     *
     * @return the expiry time
     */
    public LocalDateTime getActivationTokenExpiry() {
        return activationTokenExpiry;
    }

    /**
     * Sets the activation token expiry.
     *
     * @param expiry the expiry time to set
     */
    public void setActivationTokenExpiry(final LocalDateTime expiry) {
        this.activationTokenExpiry = expiry;
    }

    /**
     * Returns the creation timestamp.
     *
     * @return the created time
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns the last updated timestamp.
     *
     * @return the updated time
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
