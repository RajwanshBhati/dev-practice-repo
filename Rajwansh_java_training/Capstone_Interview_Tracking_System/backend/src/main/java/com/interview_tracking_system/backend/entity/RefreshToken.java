package com.interview_tracking_system.backend.entity;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Stores refresh tokens issued during login.
 */
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    /** Primary key. */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** Token string stored in database. */
    @Column(name = "token", nullable = false, unique = true)
    private String token;

    /** User this refresh token belongs to. */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Expiry timestamp of refresh token. */
    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    /**
     * Default constructor.
     */
    public RefreshToken() {
    }

    /**
     * Creates refresh token with all fields.
     *
     * @param refreshTokenId  refresh token ID
     * @param refreshToken    token value
     * @param tokenUser       user entity
     * @param tokenExpiryDate token expiry date
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "JPA relationship entity reference is intentionally stored.")
    public RefreshToken(
            final UUID refreshTokenId,
            final String refreshToken,
            final User tokenUser,
            final LocalDateTime tokenExpiryDate) {
        this.id = refreshTokenId;
        this.token = refreshToken;
        this.user = tokenUser;
        this.expiryDate = tokenExpiryDate;
    }

    /**
     * Returns refresh token ID.
     *
     * @return refresh token ID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets refresh token ID.
     *
     * @param refreshTokenId refresh token ID
     */
    public void setId(final UUID refreshTokenId) {
        this.id = refreshTokenId;
    }

    /**
     * Returns token value.
     *
     * @return token value
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets token value.
     *
     * @param refreshToken token value
     */
    public void setToken(final String refreshToken) {
        this.token = refreshToken;
    }

    /**
     * Returns user entity.
     *
     * @return user entity
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "JPA relationship entity reference is intentionally returned.")
    public User getUser() {
        return user;
    }

    /**
     * Sets user entity.
     *
     * @param tokenUser user entity
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "JPA relationship entity reference is intentionally stored.")
    public void setUser(final User tokenUser) {
        this.user = tokenUser;
    }

    /**
     * Returns expiry date.
     *
     * @return expiry date
     */
    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    /**
     * Sets expiry date.
     *
     * @param tokenExpiryDate expiry date
     */
    public void setExpiryDate(final LocalDateTime tokenExpiryDate) {
        this.expiryDate = tokenExpiryDate;
    }

    /**
     * Checks whether refresh token has expired.
     *
     * @return true if token is expired
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiryDate);
    }
}
