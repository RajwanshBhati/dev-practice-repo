package com.interview_tracking_system.backend.dto;

/**
 * Request DTO used by panel to submit interview feedback.
 */
public class SubmitFeedbackRequestDTO {

    /**
     * Interview ID for which feedback is being submitted.
     */
    private Long interviewId;

    /**
     * Panel comments about the candidate.
     */
    private String comments;

    /**
     * Strengths observed during interview.
     */
    private String strengths;

    /**
     * Weaknesses observed during interview.
     */
    private String weaknesses;

    /**
     * Areas covered during interview.
     */
    private String areasCovered;

    /**
     * Rating given by panel (1 to 5).
     */
    private Integer rating;

    /**
     * Final panel decision (SELECTED / REJECTED).
     */
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
