package com.interview_tracking_system.backend.entity;

import com.interview_tracking_system.backend.enums.Gender;
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

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a user in the interview tracking system.
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

    /** Date of birth of the user. */
    @Column
    private LocalDate dateOfBirth;

    /** Gender of the user. */
    @Enumerated(EnumType.STRING)
    private Gender gender;

    /** Mobile number of the user. */
    @Column(unique = true)
    private String mobile;

    /** Encrypted password of the user. */
    @Column(nullable = false)
    private String password;

    /** Role of the user in the system. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /** Current status of the user account. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    /** Organisation the user belongs to. */
    @Column
    private String organisation;

    /** Job designation of the user. */
    @Column
    private String designation;

    /** Activation token for account setup or verification. */
    @Column(name = "activation_token")
    private String activationToken;

    /** Expiry time of activation token. */
    @Column(name = "activation_token_expiry")
    private LocalDateTime activationTokenExpiry;

    /** Timestamp when user was created. */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /** Timestamp when user was last updated. */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Default constructor for JPA.
     */
    public User() {
    }

    /**
     * Creates a user with required account details.
     *
     * @param userName         user name
     * @param userEmail        user email
     * @param userMobile       user mobile
     * @param userPassword     encrypted password
     * @param userRole         user role
     * @param userStatus       user status
     * @param userOrganisation user organisation
     * @param userDesignation  user designation
     * @param token            activation token
     * @param tokenExpiry      activation token expiry
     */
    @SuppressWarnings("checkstyle:ParameterNumber")
    public User(
            final String userName,
            final String userEmail,
            final String userMobile,
            final String userPassword,
            final Role userRole,
            final UserStatus userStatus,
            final String userOrganisation,
            final String userDesignation,
            final String token,
            final LocalDateTime tokenExpiry) {
        this.name = userName;
        this.email = userEmail;
        this.mobile = userMobile;
        this.password = userPassword;
        this.role = userRole;
        this.status = userStatus == null ? UserStatus.ACTIVE : userStatus;
        this.organisation = userOrganisation;
        this.designation = userDesignation;
        this.activationToken = token;
        this.activationTokenExpiry = tokenExpiry;
    }

    /**
     * Returns user ID.
     *
     * @return user ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets user ID.
     *
     * @param userId user ID
     */
    public void setId(final Long userId) {
        this.id = userId;
    }

    /**
     * Returns user name.
     *
     * @return user name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets user name.
     *
     * @param userName user name
     */
    public void setName(final String userName) {
        this.name = userName;
    }

    /**
     * Returns email.
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param userEmail email
     */
    public void setEmail(final String userEmail) {
        this.email = userEmail;
    }

    /**
     * Returns date of birth.
     *
     * @return date of birth
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets date of birth.
     *
     * @param dob date of birth
     */
    public void setDateOfBirth(final LocalDate dob) {
        this.dateOfBirth = dob;
    }

    /**
     * Returns gender.
     *
     * @return gender
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Sets gender.
     *
     * @param userGender gender
     */
    public void setGender(final Gender userGender) {
        this.gender = userGender;
    }

    /**
     * Returns mobile number.
     *
     * @return mobile number
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Sets mobile number.
     *
     * @param userMobile mobile number
     */
    public void setMobile(final String userMobile) {
        this.mobile = userMobile;
    }

    /**
     * Returns password.
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param userPassword password
     */
    public void setPassword(final String userPassword) {
        this.password = userPassword;
    }

    /**
     * Returns role.
     *
     * @return role
     */
    public Role getRole() {
        return role;
    }

    /**
     * Sets role.
     *
     * @param userRole role
     */
    public void setRole(final Role userRole) {
        this.role = userRole;
    }

    /**
     * Returns user status.
     *
     * @return user status
     */
    public UserStatus getStatus() {
        return status;
    }

    /**
     * Sets user status.
     *
     * @param userStatus status
     */
    public void setStatus(final UserStatus userStatus) {
        this.status = userStatus;
    }

    /**
     * Returns organisation.
     *
     * @return organisation
     */
    public String getOrganisation() {
        return organisation;
    }

    /**
     * Sets organisation.
     *
     * @param userOrganisation organisation
     */
    public void setOrganisation(final String userOrganisation) {
        this.organisation = userOrganisation;
    }

    /**
     * Returns designation.
     *
     * @return designation
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * Sets designation.
     *
     * @param userDesignation designation
     */
    public void setDesignation(final String userDesignation) {
        this.designation = userDesignation;
    }

    /**
     * Returns activation token.
     *
     * @return activation token
     */
    public String getActivationToken() {
        return activationToken;
    }

    /**
     * Sets activation token.
     *
     * @param token activation token
     */
    public void setActivationToken(final String token) {
        this.activationToken = token;
    }

    /**
     * Returns activation token expiry.
     *
     * @return activation token expiry
     */
    public LocalDateTime getActivationTokenExpiry() {
        return activationTokenExpiry;
    }

    /**
     * Sets activation token expiry.
     *
     * @param tokenExpiry activation token expiry
     */
    public void setActivationTokenExpiry(final LocalDateTime tokenExpiry) {
        this.activationTokenExpiry = tokenExpiry;
    }

    /**
     * Returns created timestamp.
     *
     * @return created timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns updated timestamp.
     *
     * @return updated timestamp
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
