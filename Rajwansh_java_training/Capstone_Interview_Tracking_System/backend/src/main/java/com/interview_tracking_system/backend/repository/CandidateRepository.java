package com.interview_tracking_system.backend.repository;

import com.interview_tracking_system.backend.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository interface for Candidate entity.
 */
@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    /**
     * Checks if a candidate exists with the given email.
     *
     * @param email the email to check
     * @return true if exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Checks if a candidate exists with the given mobile number.
     *
     * @param mobile the mobile number to check
     * @return true if exists, false otherwise
     */
    boolean existsByMobile(String mobile);

    /**
     * Finds a candidate by their linked candidate user ID.
     *
     * @param candidateUserId the candidate user ID
     * @return an Optional containing the candidate if found
     */
    Optional<Candidate> findByCandidateUserId(Long candidateUserId);

    /**
     * Finds a candidate by email address.
     *
     * @param email the email to search
     * @return an Optional containing the candidate if found
     */
    Optional<Candidate> findByEmail(String email);
}
