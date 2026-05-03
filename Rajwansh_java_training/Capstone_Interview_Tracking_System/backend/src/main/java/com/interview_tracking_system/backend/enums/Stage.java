package com.interview_tracking_system.backend.enums;

/**
 * Represents the recruitment stage of a candidate.
 */
public enum Stage {

    /**
     * Candidate not applied.
     */
    NOT_APPLIED,
    /**
     * Candidate has submitted profile details and resume.
     */
    PROFILING,

    /**
     * HR is reviewing the candidate before technical rounds.
     */
    SCREENING,

    /**
     * Candidate is scheduled or moved to first technical round.
     */
    L1_TECHNICAL,

    /**
     * Candidate is scheduled or moved to second technical round.
     */
    L2_TECHNICAL,

    /**
     * Candidate is moved to the final HR discussion round.
     */
    HR_ROUND,

    /**
     * Candidate is rejected from the hiring process.
     */
    REJECTED,

    /**
     * Candidate is finally selected after all required rounds.
     */
    SELECTED
}
