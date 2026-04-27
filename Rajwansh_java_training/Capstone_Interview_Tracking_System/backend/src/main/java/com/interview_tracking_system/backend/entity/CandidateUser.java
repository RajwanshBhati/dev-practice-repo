package com.interview_tracking_system.backend.entity;

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
     * Returns the unique identifier.
     *
     * @return the candidate user ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the full name.
     *
     * @return the full name of the candidate user
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the full name.
     *
     * @param name the full name to set
     */
    public void setFullName(final String name) {
        this.fullName = name;
    }

    /**
     * Returns the email address.
     *
     * @return the email of the candidate user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address.
     *
     * @param userEmail the email to set
     */
    public void setEmail(final String userEmail) {
        this.email = userEmail;
    }

    /**
     * Returns the encoded password.
     *
     * @return the password of the candidate user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the encoded password.
     *
     * @param userPassword the password to set
     */
    public void setPassword(final String userPassword) {
        this.password = userPassword;
    }

    /**
     * Returns the linked candidate profile.
     *
     * @return the candidate profile of this user
     */
    public Candidate getCandidate() {
        return candidate;
    }

    /**
     * Sets the linked candidate profile.
     *
     * @param candidateProfile the candidate profile to set
     */
    public void setCandidate(final Candidate candidateProfile) {
        this.candidate = candidateProfile;
    }
}
