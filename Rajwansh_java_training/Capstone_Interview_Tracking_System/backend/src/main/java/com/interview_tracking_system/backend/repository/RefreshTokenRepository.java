package com.interview_tracking_system.backend.repository;

import com.interview_tracking_system.backend.entity.RefreshToken;
import com.interview_tracking_system.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository for RefreshToken entity.
 * Handles persistence of JWT refresh tokens.
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    /**
     * Find refresh token by token string.
     * Used during token refresh validation.
     *
     * @param token the token string to search
     * @return an Optional containing the refresh token if found
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Find refresh token mapped to a user.
     *
     * @param user the user to search by
     * @return an Optional containing the refresh token if found
     */
    Optional<RefreshToken> findByUser(User user);

    /**
     * Delete refresh token(s) for a user during logout.
     *
     * @param user the user whose tokens should be deleted
     */
    @Transactional
    @Modifying
    void deleteByUser(User user);
}
