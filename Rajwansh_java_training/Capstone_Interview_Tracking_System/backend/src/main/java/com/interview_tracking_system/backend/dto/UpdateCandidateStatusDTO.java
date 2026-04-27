package com.interview_tracking_system.backend.dto;

/**
 * Request DTO used by HR to update candidate status or stage.
 */
public class UpdateCandidateStatusDTO {

    /**
     * Candidate ID whose status needs to be updated.
     */
    private Long candidateId;

    /**
     * New stage of the candidate (SCREENING, L1_TECHNICAL, L2_TECHNICAL, etc.)
     */
    private String stage;

    /**
     * Final decision.
     */
    private String decision;

    /**
     * Optional remarks by HR.
     */
    private String remarks;

    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
