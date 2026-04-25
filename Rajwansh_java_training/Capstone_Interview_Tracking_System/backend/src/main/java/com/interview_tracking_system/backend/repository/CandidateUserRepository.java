package com.interview_tracking_system.backend.repository;

import com.interview_tracking_system.backend.entity.CandidateUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository interface for CandidateUser entity.
 */
@Repository
public interface CandidateUserRepository extends JpaRepository<CandidateUser, Long> {

    /**
     * Finds a candidate user by email address.
     *
     * @param email the email address to search
     * @return an Optional containing the candidate user if found
     */
    Optional<CandidateUser> findByEmail(String email);

    /**
     * Checks if a candidate user exists with the given email.
     *
     * @param email the email address to check
     * @return true if exists, false otherwise
     */
    boolean existsByEmail(String email);
}
