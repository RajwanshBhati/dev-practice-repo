package com.interview_tracking_system.backend.dto;

/**
 * Request DTO used by HR to update candidate status or stage.
 */
public class UpdateCandidateStatusDTO {

    /** Candidate ID whose status needs to be updated. */
    private Long candidateId;

    /** New stage of the candidate. */
    private String stage;

    /** Final decision. */
    private String decision;

    /** Optional remarks by HR. */
    private String remarks;

    /**
     * Returns candidate ID.
     *
     * @return candidate ID
     */
    public Long getCandidateId() {
        return candidateId;
    }

    /**
     * Sets candidate ID.
     *
     * @param id candidate ID
     */
    public void setCandidateId(final Long id) {
        this.candidateId = id;
    }

    /**
     * Returns stage.
     *
     * @return stage
     */
    public String getStage() {
        return stage;
    }

    /**
     * Sets stage.
     *
     * @param candidateStage stage
     */
    public void setStage(final String candidateStage) {
        this.stage = candidateStage;
    }

    /**
     * Returns decision.
     *
     * @return decision
     */
    public String getDecision() {
        return decision;
    }

    /**
     * Sets decision.
     *
     * @param candidateDecision decision
     */
    public void setDecision(final String candidateDecision) {
        this.decision = candidateDecision;
    }

    /**
     * Returns remarks.
     *
     * @return remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * Sets remarks.
     *
     * @param hrRemarks remarks
     */
    public void setRemarks(final String hrRemarks) {
        this.remarks = hrRemarks;
    }
}
