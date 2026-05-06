package com.interview_tracking_system.backend.entity;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Represents a registered candidate user in the system.
 */
@Entity
@Table(name = "candidate_users", indexes = {
        @Index(name = "idx_candidate_user_email", columnList = "email")
})
public class CandidateUser {

    /** Unique identifier for the candidate user. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Full name of the candidate user. */
    @Column(name = "full_name", nullable = false)
    private String fullName;

    /** Unique email address of the candidate user. */
    @Column(unique = true, nullable = false)
    private String email;

    /** BCrypt encoded password of the candidate user. */
    @Column(nullable = false)
    private String password;

    /** Candidate profile linked to this user, if applied. */
    @OneToOne(mappedBy = "candidateUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Candidate candidate;

    /**
     * Returns candidate user ID.
     *
     * @return candidate user ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets candidate user ID.
     *
     * @param candidateUserId candidate user ID
     */
    public void setId(final Long candidateUserId) {
        this.id = candidateUserId;
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
     * Returns encoded password.
     *
     * @return encoded password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets encoded password.
     *
     * @param userPassword encoded password
     */
    public void setPassword(final String userPassword) {
        this.password = userPassword;
    }

    /**
     * Returns linked candidate profile.
     *
     * @return candidate profile
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "JPA relationship entity reference is intentionally returned.")
    public Candidate getCandidate() {
        return candidate;
    }

    /**
     * Sets linked candidate profile.
     *
     * @param candidateProfile candidate profile
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "JPA relationship entity reference is intentionally stored.")
    public void setCandidate(final Candidate candidateProfile) {
        this.candidate = candidateProfile;
    }
}
