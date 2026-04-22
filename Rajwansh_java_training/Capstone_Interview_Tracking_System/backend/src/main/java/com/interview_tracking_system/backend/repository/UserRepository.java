package com.interview_tracking_system.backend.repository;

import com.interview_tracking_system.backend.entity.User;
import com.interview_tracking_system.backend.enums.Role;
import com.interview_tracking_system.backend.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for User entity.
 * Handles HR and Panel user persistence operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email
     */

    Optional<User> findByEmail(String email);

    /**
     * Find user by mobile
     */

    Optional<User> findByMobile(String mobile);

    /**
     * Find user by activation token
     */

    Optional<User> findByActivationToken(String activationToken);

    /**
     * Check if email already exists
     */

    boolean existsByEmail(String email);

    /**
     * Check if mobile already exists
     */

    boolean existsByMobile(String mobile);

    /**
     * Find users by role
     */

    List<User> findByRole(Role role);

    /**
     * Find users by role and status
     */

    List<User> findByRoleAndStatus(Role role, UserStatus status);
}
