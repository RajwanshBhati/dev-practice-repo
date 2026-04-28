package com.interview_tracking_system.backend.dto;

/**
 * Response DTO used to show panel feedback to HR
 */
public class HRFeedbackDTO {

    /**
     * Feedback id
     */
    private Long feedbackId;

    /**
     * Interview id
     */
    private Long interviewId;

    /**
     * Candidate id
     */
    private Long candidateId;

    /**
     * Candidate name
     */
    private String candidateName;

    /**
     * Panel user id
     */
    private Long panelId;

    /**
     * Panel name
     */
    private String panelName;

    /**
     * Panel email
     */
    private String panelEmail;

    /**
     * Interview stage
     */
    private String stage;

    /**
     * Interview date
     */
    private String interviewDate;

    /**
     * Interview time
     */
    private String interviewTime;

    /**
     * Feedback comments
     */
    private String comments;

    /**
     * Candidate strengths
     */
    private String strengths;

    /**
     * Candidate weaknesses
     */
    private String weaknesses;

    /**
     * Feedback rating
     */
    private Integer rating;

    /**
     * Panel decision
     */
    private String decision;

    /**
     * Returns feedback id
     */
    public Long getFeedbackId() {
        return feedbackId;
    }

    /**
     * Sets feedback id
     */
    public void setFeedbackId(final Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    /**
     * Returns interview id
     */
    public Long getInterviewId() {
        return interviewId;
    }

    /**
     * Sets interview id
     */
    public void setInterviewId(final Long interviewId) {
        this.interviewId = interviewId;
    }

    /**
     * Returns candidate id
     */
    public Long getCandidateId() {
        return candidateId;
    }

    /**
     * Sets candidate id
     */
    public void setCandidateId(final Long candidateId) {
        this.candidateId = candidateId;
    }

    /**
     * Returns candidate name
     */
    public String getCandidateName() {
        return candidateName;
    }

    /**
     * Sets candidate name
     */
    public void setCandidateName(final String candidateName) {
        this.candidateName = candidateName;
    }

    /**
     * Returns panel id
     */
    public Long getPanelId() {
        return panelId;
    }

    /**
     * Sets panel id
     */
    public void setPanelId(final Long panelId) {
        this.panelId = panelId;
    }

    /**
     * Returns panel name
     */
    public String getPanelName() {
        return panelName;
    }

    /**
     * Sets panel name
     */
    public void setPanelName(final String panelName) {
        this.panelName = panelName;
    }

    /**
     * Returns panel email
     */
    public String getPanelEmail() {
        return panelEmail;
    }

    /**
     * Sets panel email
     */
    public void setPanelEmail(final String panelEmail) {
        this.panelEmail = panelEmail;
    }

    /**
     * Returns interview stage
     */
    public String getStage() {
        return stage;
    }

    /**
     * Sets interview stage
     */
    public void setStage(final String stage) {
        this.stage = stage;
    }

    /**
     * Returns interview date
     */
    public String getInterviewDate() {
        return interviewDate;
    }

    /**
     * Sets interview date
     */
    public void setInterviewDate(final String interviewDate) {
        this.interviewDate = interviewDate;
    }

    /**
     * Returns interview time
     */
    public String getInterviewTime() {
        return interviewTime;
    }

    /**
     * Sets interview time
     */
    public void setInterviewTime(final String interviewTime) {
        this.interviewTime = interviewTime;
    }

    /**
     * Returns feedback comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets feedback comments
     */
    public void setComments(final String comments) {
        this.comments = comments;
    }

    /**
     * Returns candidate strengths
     */
    public String getStrengths() {
        return strengths;
    }

    /**
     * Sets candidate strengths
     */
    public void setStrengths(final String strengths) {
        this.strengths = strengths;
    }

    /**
     * Returns candidate weaknesses
     */
    public String getWeaknesses() {
        return weaknesses;
    }

    /**
     * Sets candidate weaknesses
     */
    public void setWeaknesses(final String weaknesses) {
        this.weaknesses = weaknesses;
    }

    /**
     * Returns feedback rating
     */
    public Integer getRating() {
        return rating;
    }

    /**
     * Sets feedback rating
     */
    public void setRating(final Integer rating) {
        this.rating = rating;
    }

    /**
     * Returns panel decision
     */
    public String getDecision() {
        return decision;
    }

    /**
     * Sets panel decision
     */
    public void setDecision(final String decision) {
        this.decision = decision;
    }
}
