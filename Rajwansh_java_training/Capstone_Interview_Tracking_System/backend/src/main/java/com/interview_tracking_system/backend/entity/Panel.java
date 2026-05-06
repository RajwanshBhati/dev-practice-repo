package com.interview_tracking_system.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Represents a panel member in the interview tracking system.
 */
@Entity
@Table(name = "panel", indexes = {
        @Index(name = "idx_panel_email", columnList = "email")
})
public class Panel {

    /** Maximum character length for name and designation fields. */
    private static final int NAME_LENGTH = 100;

    /** Maximum character length for email fields. */
    private static final int EMAIL_LENGTH = 150;

    /** Maximum character length for mobile number fields. */
    private static final int MOBILE_LENGTH = 15;

    /** Maximum character length for organization name fields. */
    private static final int ORG_LENGTH = 150;

    /** Maximum character length for password and token fields. */
    private static final int TOKEN_LENGTH = 255;

    /** Unique identifier for the panel member. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Full name of the panel member. */
    @Column(name = "full_name", nullable = false, length = NAME_LENGTH)
    private String fullName;

    /** Email address of the panel member. */
    @Column(nullable = false, unique = true, length = EMAIL_LENGTH)
    private String email;

    /** Mobile phone number of the panel member. */
    @Column(length = MOBILE_LENGTH)
    private String mobile;

    /** Organization the panel member belongs to. */
    @Column(length = ORG_LENGTH)
    private String organization;

    /** Job designation of the panel member. */
    @Column(length = NAME_LENGTH)
    private String designation;

    /** Hashed password used for authentication. */
    @Column(length = TOKEN_LENGTH)
    private String password;

    /** Indicates whether the panel member account is active. */
    @Column(nullable = false)
    private boolean active = false;

    /** Activation token sent to the panel member email. */
    @Column(length = TOKEN_LENGTH)
    private String activationToken;

    /** Expiry date and time of the activation token. */
    private LocalDateTime tokenExpiry;

    /** Timestamp when this panel member was created. */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /** Timestamp when this panel member was last updated. */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Returns panel ID.
     *
     * @return panel ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets panel ID.
     *
     * @param panelId panel ID
     */
    public void setId(final Long panelId) {
        this.id = panelId;
    }

    /**
     * Returns full name.
     *
     * @return full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets full name.
     *
     * @param name full name
     */
    public void setFullName(final String name) {
        this.fullName = name;
    }

    /**
     * Returns email address.
     *
     * @return email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email address.
     *
     * @param userEmail email address
     */
    public void setEmail(final String userEmail) {
        this.email = userEmail;
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
     * @param mobileNumber mobile number
     */
    public void setMobile(final String mobileNumber) {
        this.mobile = mobileNumber;
    }

    /**
     * Returns organization.
     *
     * @return organization
     */
    public String getOrganization() {
        return organization;
    }

    /**
     * Sets organization.
     *
     * @param org organization
     */
    public void setOrganization(final String org) {
        this.organization = org;
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
     * @param panelDesignation designation
     */
    public void setDesignation(final String panelDesignation) {
        this.designation = panelDesignation;
    }

    /**
     * Returns hashed password.
     *
     * @return hashed password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets hashed password.
     *
     * @param hashedPassword hashed password
     */
    public void setPassword(final String hashedPassword) {
        this.password = hashedPassword;
    }

    /**
     * Returns whether account is active.
     *
     * @return true if active otherwise false
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets account active status.
     *
     * @param accountActive active status
     */
    public void setActive(final boolean accountActive) {
        this.active = accountActive;
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
     * Returns token expiry timestamp.
     *
     * @return token expiry timestamp
     */
    public LocalDateTime getTokenExpiry() {
        return tokenExpiry;
    }

    /**
     * Sets token expiry timestamp.
     *
     * @param expiry token expiry timestamp
     */
    public void setTokenExpiry(final LocalDateTime expiry) {
        this.tokenExpiry = expiry;
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
     * Sets created timestamp.
     *
     * @param created created timestamp
     */
    public void setCreatedAt(final LocalDateTime created) {
        this.createdAt = created;
    }

    /**
     * Returns updated timestamp.
     *
     * @return updated timestamp
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets updated timestamp.
     *
     * @param updated updated timestamp
     */
    public void setUpdatedAt(final LocalDateTime updated) {
        this.updatedAt = updated;
    }
}
