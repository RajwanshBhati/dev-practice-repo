package com.interview_tracking_system.backend.repository;

import com.interview_tracking_system.backend.entity.User;
import com.interview_tracking_system.backend.enums.Role;
import com.interview_tracking_system.backend.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for User entity.
 * Handles HR and Panel user persistence operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email.
     *
     * @param email the email to search
     * @return an Optional containing the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user by email (case-insensitive).
     *
     * @param email the email to search
     * @return an Optional containing the user if found
     */
    Optional<User> findByEmailIgnoreCase(String email);

    /**
     * Find user by mobile.
     *
     * @param mobile the mobile number to search
     * @return an Optional containing the user if found
     */
    Optional<User> findByMobile(String mobile);

    /**
     * Find user by activation token.
     *
     * @param activationToken the activation token to search
     * @return an Optional containing the user if found
     */
    Optional<User> findByActivationToken(String activationToken);

    /**
     * Check if email already exists.
     *
     * @param email the email to check
     * @return true if exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Check if mobile already exists.
     *
     * @param mobile the mobile number to check
     * @return true if exists, false otherwise
     */
    boolean existsByMobile(String mobile);

    /**
     * Find users by role.
     *
     * @param role the role to filter by
     * @return list of users with the given role
     */
    List<User> findByRole(Role role);

    /**
     * Find users by role and status.
     *
     * @param role   the role to filter by
     * @param status the status to filter by
     * @return list of users matching role and status
     */
    List<User> findByRoleAndStatus(Role role, UserStatus status);

    /**
     * Find users by role ordered by creation date descending.
     *
     * @param role the role to filter by
     * @return list of users with the given role newest first
     */
    List<User> findByRoleOrderByCreatedAtDesc(Role role);
}
