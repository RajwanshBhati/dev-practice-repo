package com.interview_tracking_system.backend.dto;

import com.interview_tracking_system.backend.enums.Stage;

/**
 * DTO for candidate profile response.
 */
public class CandidateResponseDTO {

    /** Candidate ID. */
    private Long id;

    /** Full name of the candidate. */
    private String name;

    /** Email of the candidate. */
    private String email;

    /** Current stage of the candidate. */
    private Stage status;

    /** Job description ID applied for. */
    private Long jdId;

    /**
     * Returns the candidate ID.
     *
     * @return the ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the candidate ID.
     *
     * @param candidateId the ID to set
     */
    public void setId(final Long candidateId) {
        this.id = candidateId;
    }

    /**
     * Returns the name.
     *
     * @return the candidate name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param candidateName the name to set
     */
    public void setName(final String candidateName) {
        this.name = candidateName;
    }

    /**
     * Returns the email.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email.
     *
     * @param candidateEmail the email to set
     */
    public void setEmail(final String candidateEmail) {
        this.email = candidateEmail;
    }

    /**
     * Returns the current stage.
     *
     * @return the interview stage
     */
    public Stage getStatus() {
        return status;
    }

    /**
     * Sets the current stage.
     *
     * @param candidateStatus the stage to set
     */
    public void setStatus(final Stage candidateStatus) {
        this.status = candidateStatus;
    }

    /**
     * Returns the JD ID.
     *
     * @return the job description ID
     */
    public Long getJdId() {
        return jdId;
    }

    /**
     * Sets the JD ID.
     *
     * @param jobDescriptionId the JD ID to set
     */
    public void setJdId(final Long jobDescriptionId) {
        this.jdId = jobDescriptionId;
    }
}
