package com.interview_tracking_system.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO used by panel to submit interview feedback.
 */
public class SubmitFeedbackRequestDTO {

    /** Maximum allowed rating value. */
    private static final int MAX_RATING = 5;

    /** Interview ID for which feedback is being submitted. */
    @NotNull(message = "Interview id is required")
    private Long interviewId;

    /** Panel comments about the candidate. */
    @NotBlank(message = "Comments are required")
    private String comments;

    /** Strengths observed during interview. */
    @NotBlank(message = "Strengths are required")
    private String strengths;

    /** Weaknesses observed during interview. */
    @NotBlank(message = "Weaknesses are required")
    private String weaknesses;

    /** Areas covered during interview. */
    @NotBlank(message = "Areas covered are required")
    private String areasCovered;

    /** Rating given by panel (1 to 5). */
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = MAX_RATING, message = "Rating must be between 1 and 5")
    private Integer rating;

    /** Final panel decision (SELECTED / REJECTED). */
    @NotBlank(message = "Decision is required")
    private String decision;

    /**
     * Returns interview ID.
     *
     * @return interview ID
     */
    public Long getInterviewId() {
        return interviewId;
    }

    /**
     * Sets interview ID.
     *
     * @param id interview ID
     */
    public void setInterviewId(final Long id) {
        this.interviewId = id;
    }

    /**
     * Returns comments.
     *
     * @return comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets comments.
     *
     * @param panelComments comments
     */
    public void setComments(final String panelComments) {
        this.comments = panelComments;
    }

    /**
     * Returns strengths.
     *
     * @return strengths
     */
    public String getStrengths() {
        return strengths;
    }

    /**
     * Sets strengths.
     *
     * @param candidateStrengths strengths
     */
    public void setStrengths(final String candidateStrengths) {
        this.strengths = candidateStrengths;
    }

    /**
     * Returns weaknesses.
     *
     * @return weaknesses
     */
    public String getWeaknesses() {
        return weaknesses;
    }

    /**
     * Sets weaknesses.
     *
     * @param candidateWeaknesses weaknesses
     */
    public void setWeaknesses(final String candidateWeaknesses) {
        this.weaknesses = candidateWeaknesses;
    }

    /**
     * Returns areas covered.
     *
     * @return areas covered
     */
    public String getAreasCovered() {
        return areasCovered;
    }

    /**
     * Sets areas covered.
     *
     * @param coveredAreas areas covered
     */
    public void setAreasCovered(final String coveredAreas) {
        this.areasCovered = coveredAreas;
    }

    /**
     * Returns rating.
     *
     * @return rating
     */
    public Integer getRating() {
        return rating;
    }

    /**
     * Sets rating.
     *
     * @param feedbackRating rating
     */
    public void setRating(final Integer feedbackRating) {
        this.rating = feedbackRating;
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
     * @param panelDecision decision
     */
    public void setDecision(final String panelDecision) {
        this.decision = panelDecision;
    }
}
