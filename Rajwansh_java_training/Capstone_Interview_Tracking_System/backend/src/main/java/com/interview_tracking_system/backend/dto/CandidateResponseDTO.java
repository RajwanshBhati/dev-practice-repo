package com.interview_tracking_system.backend.dto;

import com.interview_tracking_system.backend.enums.Stage;
import java.util.UUID;

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

    /** Current status of the candidate. */
    private Stage status;

    /** Job description ID applied for. */
    private UUID jdId;

    /** Stage at which candidate was rejected. */
    private Stage rejectedStage;

    /** Current stage of the candidate. */
    private Stage currentStage;

    /**
     * Returns the candidate ID.
     *
     * @return candidate ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the candidate ID.
     *
     * @param candidateId candidate ID
     */
    public void setId(final Long candidateId) {
        this.id = candidateId;
    }

    /**
     * Returns the candidate name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the candidate name.
     *
     * @param candidateName name
     */
    public void setName(final String candidateName) {
        this.name = candidateName;
    }

    /**
     * Returns the email address.
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address.
     *
     * @param candidateEmail email
     */
    public void setEmail(final String candidateEmail) {
        this.email = candidateEmail;
    }

    /**
     * Returns the current status.
     *
     * @return status
     */
    public Stage getStatus() {
        return status;
    }

    /**
     * Sets the current status.
     *
     * @param candidateStatus status
     */
    public void setStatus(final Stage candidateStatus) {
        this.status = candidateStatus;
    }

    /**
     * Returns the job description ID.
     *
     * @return JD ID
     */
    public UUID getJdId() {
        return jdId;
    }

    /**
     * Sets the job description ID.
     *
     * @param jobDescriptionId JD ID
     */
    public void setJdId(final UUID jobDescriptionId) {
        this.jdId = jobDescriptionId;
    }

    /**
     * Returns the rejected stage.
     *
     * @return rejected stage
     */
    public Stage getRejectedStage() {
        return rejectedStage;
    }

    /**
     * Sets the rejected stage.
     *
     * @param candidateRejectedStage rejected stage
     */
    public void setRejectedStage(final Stage candidateRejectedStage) {
        this.rejectedStage = candidateRejectedStage;
    }

    /**
     * Returns the current stage.
     *
     * @return current stage
     */
    public Stage getCurrentStage() {
        return currentStage;
    }

    /**
     * Sets the current stage.
     *
     * @param candidateCurrentStage current stage
     */
    public void setCurrentStage(final Stage candidateCurrentStage) {
        this.currentStage = candidateCurrentStage;
    }
}
