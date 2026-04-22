package com.interview_tracking_system.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Stores refresh tokens issued during login.
 * Used to issue new access tokens without re-login.
 * One active refresh token per user at a time.
 * Tokens have an expiry time after which they are invalid.
 */
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * The actual token string stored in DB.
     * Matched against token sent by client.
     */
    @Column(name = "token", nullable = false, unique = true)
    private String token;

    /**
     * User this refresh token belongs to.
     * One-to-one: one active token per user.
     */
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Expiry timestamp.
     * Token rejected if current time is past this value.
     */
    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    /**
     * Default constructor.
     */
    public RefreshToken() {
    }

    /**
     * All-args constructor.
     */
    public RefreshToken(UUID id, String token, User user, LocalDateTime expiryDate) {
        this.id = id;
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
    }

    /**
     * Returns the id.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the id.
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Returns the token.
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the token.
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Returns the user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Returns expiry date.
     */
    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    /**
     * Sets expiry date.
     */
    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * Checks if this refresh token has expired.
     *
     * @return true if token is expired
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiryDate);
    }
}
