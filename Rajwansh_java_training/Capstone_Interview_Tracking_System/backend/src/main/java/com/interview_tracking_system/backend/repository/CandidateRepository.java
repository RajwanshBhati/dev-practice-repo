package com.interview_tracking_system.backend.repository;

import com.interview_tracking_system.backend.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import com.interview_tracking_system.backend.enums.Stage;
import java.util.List;

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

   /**
     * Finds a candidate by email address (case-insensitive).
     *
     * <p>This is useful to avoid duplicate entries due to case differences
     * (e.g., test@gmail.com vs TEST@gmail.com).</p>
     *
     * @param email the email to search
     * @return an Optional containing the candidate if found, otherwise empty
     */
    Optional<Candidate> findByEmailIgnoreCase(String email);

    /**
     * Checks if a candidate exists with the given email (case-insensitive)
     * and whose status is not equal to the provided stage.
     *
     * <p>Useful for preventing duplicate active candidates while allowing
     * re-registration if the candidate is in a terminal stage like REJECTED.</p>
     *
     * @param email  the email to check
     * @param status the stage to exclude
     * @return true if such a candidate exists, false otherwise
     */
    boolean existsByEmailIgnoreCaseAndStatusNot(String email, Stage status);

    /**
     * Finds the most recent candidate entry by email (case-insensitive).
     *
     * <p>Results are ordered by ID in descending order and only the latest
     * record is returned.</p>
     *
     * @param email the email to search
     * @return an Optional containing the latest candidate record if found
     */
    Optional<Candidate> findTopByEmailIgnoreCaseOrderByIdDesc(String email);

    /**
     * Finds all candidate records by email (case-insensitive),
     * ordered by most recent first.
     *
     * <p>This is useful for tracking candidate history (multiple applications).</p>
     *
     * @param email the email to search
     * @return list of candidates sorted by ID in descending order
     */
    List<Candidate> findByEmailIgnoreCaseOrderByIdDesc(String email);
}
