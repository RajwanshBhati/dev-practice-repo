package com.interview_tracking_system.backend.dto;

/**
 * Response DTO used to show panel feedback to HR.
 */
public class HRFeedbackDTO {

    /**
     * Feedback id.
     */
    private Long feedbackId;

    /**
     * Interview id.
     */
    private Long interviewId;

    /**
     * Candidate id.
     */
    private Long candidateId;

    /**
     * Candidate name.
     */
    private String candidateName;

    /**
     * Panel user id.
     */
    private Long panelId;

    /**
     * Panel name.
     */
    private String panelName;

    /**
     * Panel email.
     */
    private String panelEmail;

    /**
     * Interview stage.
     */
    private String stage;

    /**
     * Interview date.
     */
    private String interviewDate;

    /**
     * Interview time.
     */
    private String interviewTime;

    /**
     * Feedback comments.
     */
    private String comments;

    /**
     * Candidate strengths.
     */
    private String strengths;

    /**
     * Candidate weaknesses.
     */
    private String weaknesses;

    /**
     * Feedback rating.
     */
    private Integer rating;

    /**
     * Panel decision.
     */
    private String decision;

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(final Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public Long getInterviewId() {
        return interviewId;
    }

    public void setInterviewId(final Long interviewId) {
        this.interviewId = interviewId;
    }

    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(final Long candidateId) {
        this.candidateId = candidateId;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(final String candidateName) {
        this.candidateName = candidateName;
    }

    public Long getPanelId() {
        return panelId;
    }

    public void setPanelId(final Long panelId) {
        this.panelId = panelId;
    }

    public String getPanelName() {
        return panelName;
    }

    public void setPanelName(final String panelName) {
        this.panelName = panelName;
    }

    public String getPanelEmail() {
        return panelEmail;
    }

    public void setPanelEmail(final String panelEmail) {
        this.panelEmail = panelEmail;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(final String stage) {
        this.stage = stage;
    }

    public String getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(final String interviewDate) {
        this.interviewDate = interviewDate;
    }

    public String getInterviewTime() {
        return interviewTime;
    }

    public void setInterviewTime(final String interviewTime) {
        this.interviewTime = interviewTime;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(final String comments) {
        this.comments = comments;
    }

    public String getStrengths() {
        return strengths;
    }

    public void setStrengths(final String strengths) {
        this.strengths = strengths;
    }

    public String getWeaknesses() {
        return weaknesses;
    }

    public void setWeaknesses(final String weaknesses) {
        this.weaknesses = weaknesses;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(final Integer rating) {
        this.rating = rating;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(final String decision) {
        this.decision = decision;
    }
}
