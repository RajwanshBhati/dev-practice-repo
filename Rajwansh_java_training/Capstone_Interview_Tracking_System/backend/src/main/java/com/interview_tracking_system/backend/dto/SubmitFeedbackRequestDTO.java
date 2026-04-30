package com.interview_tracking_system.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO used by panel to submit interview feedback.
 */
public class SubmitFeedbackRequestDTO {

    /**
     * Interview ID for which feedback is being submitted.
     */
    @NotNull(message = "Interview id is required")
    private Long interviewId;

    /**
     * Panel comments about the candidate.
     */
    @NotBlank(message = "Comments are required")
    private String comments;

    /**
     * Strengths observed during interview.
     */
    @NotBlank(message = "Strengths are required")
    private String strengths;

    /**
     * Weaknesses observed during interview.
     */
    @NotBlank(message = "Weaknesses are required")
    private String weaknesses;

    /**
     * Areas covered during interview.
     */
    @NotBlank(message = "Areas covered are required")
    private String areasCovered;

    /**
     * Rating given by panel (1 to 5).
     */
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    private Integer rating;

    /**
     * Final panel decision (SELECTED / REJECTED).
     */
    @NotBlank(message = "Decision is required")
    private String decision;

    public Long getInterviewId() {
        return interviewId;
    }

    public void setInterviewId(Long interviewId) {
        this.interviewId = interviewId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getStrengths() {
        return strengths;
    }

    public void setStrengths(String strengths) {
        this.strengths = strengths;
    }

    public String getWeaknesses() {
        return weaknesses;
    }

    public void setWeaknesses(String weaknesses) {
        this.weaknesses = weaknesses;
    }

    public String getAreasCovered() {
        return areasCovered;
    }

    public void setAreasCovered(String areasCovered) {
        this.areasCovered = areasCovered;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }
}
