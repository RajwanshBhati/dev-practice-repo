package com.interview_tracking_system.backend.dto;

/**
 * Response DTO used to show panel feedback to HR.
 */
public class HRFeedbackDTO {

    /** Feedback ID. */
    private Long feedbackId;

    /** Interview ID. */
    private Long interviewId;

    /** Candidate ID. */
    private Long candidateId;

    /** Candidate name. */
    private String candidateName;

    /** Panel user ID. */
    private Long panelId;

    /** Panel name. */
    private String panelName;

    /** Panel email. */
    private String panelEmail;

    /** Interview stage. */
    private String stage;

    /** Interview date. */
    private String interviewDate;

    /** Interview time. */
    private String interviewTime;

    /** Feedback comments. */
    private String comments;

    /** Candidate strengths. */
    private String strengths;

    /** Candidate weaknesses. */
    private String weaknesses;

    /** Feedback rating. */
    private Integer rating;

    /** Panel decision. */
    private String decision;

    /**
     * Returns feedback ID.
     *
     * @return feedback ID
     */
    public Long getFeedbackId() {
        return feedbackId;
    }

    /**
     * Sets feedback ID.
     *
     * @param feedbackId feedback ID
     */
    public void setFeedbackId(final Long feedbackId) {
        this.feedbackId = feedbackId;
    }

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
     * @param interviewId interview ID
     */
    public void setInterviewId(final Long interviewId) {
        this.interviewId = interviewId;
    }

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
     * @param candidateId candidate ID
     */
    public void setCandidateId(final Long candidateId) {
        this.candidateId = candidateId;
    }

    /**
     * Returns candidate name.
     *
     * @return candidate name
     */
    public String getCandidateName() {
        return candidateName;
    }

    /**
     * Sets candidate name.
     *
     * @param candidateName candidate name
     */
    public void setCandidateName(final String candidateName) {
        this.candidateName = candidateName;
    }

    /**
     * Returns panel ID.
     *
     * @return panel ID
     */
    public Long getPanelId() {
        return panelId;
    }

    /**
     * Sets panel ID.
     *
     * @param panelId panel ID
     */
    public void setPanelId(final Long panelId) {
        this.panelId = panelId;
    }

    /**
     * Returns panel name.
     *
     * @return panel name
     */
    public String getPanelName() {
        return panelName;
    }

    /**
     * Sets panel name.
     *
     * @param panelName panel name
     */
    public void setPanelName(final String panelName) {
        this.panelName = panelName;
    }

    /**
     * Returns panel email.
     *
     * @return panel email
     */
    public String getPanelEmail() {
        return panelEmail;
    }

    /**
     * Sets panel email.
     *
     * @param panelEmail panel email
     */
    public void setPanelEmail(final String panelEmail) {
        this.panelEmail = panelEmail;
    }

    /**
     * Returns interview stage.
     *
     * @return stage
     */
    public String getStage() {
        return stage;
    }

    /**
     * Sets interview stage.
     *
     * @param stage interview stage
     */
    public void setStage(final String stage) {
        this.stage = stage;
    }

    /**
     * Returns interview date.
     *
     * @return interview date
     */
    public String getInterviewDate() {
        return interviewDate;
    }

    /**
     * Sets interview date.
     *
     * @param interviewDate interview date
     */
    public void setInterviewDate(final String interviewDate) {
        this.interviewDate = interviewDate;
    }

    /**
     * Returns interview time.
     *
     * @return interview time
     */
    public String getInterviewTime() {
        return interviewTime;
    }

    /**
     * Sets interview time.
     *
     * @param interviewTime interview time
     */
    public void setInterviewTime(final String interviewTime) {
        this.interviewTime = interviewTime;
    }

    /**
     * Returns feedback comments.
     *
     * @return comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets feedback comments.
     *
     * @param comments feedback comments
     */
    public void setComments(final String comments) {
        this.comments = comments;
    }

    /**
     * Returns candidate strengths.
     *
     * @return strengths
     */
    public String getStrengths() {
        return strengths;
    }

    /**
     * Sets candidate strengths.
     *
     * @param strengths candidate strengths
     */
    public void setStrengths(final String strengths) {
        this.strengths = strengths;
    }

    /**
     * Returns candidate weaknesses.
     *
     * @return weaknesses
     */
    public String getWeaknesses() {
        return weaknesses;
    }

    /**
     * Sets candidate weaknesses.
     *
     * @param weaknesses candidate weaknesses
     */
    public void setWeaknesses(final String weaknesses) {
        this.weaknesses = weaknesses;
    }

    /**
     * Returns feedback rating.
     *
     * @return rating
     */
    public Integer getRating() {
        return rating;
    }

    /**
     * Sets feedback rating.
     *
     * @param rating feedback rating
     */
    public void setRating(final Integer rating) {
        this.rating = rating;
    }

    /**
     * Returns panel decision.
     *
     * @return decision
     */
    public String getDecision() {
        return decision;
    }

    /**
     * Sets panel decision.
     *
     * @param decision panel decision
     */
    public void setDecision(final String decision) {
        this.decision = decision;
    }
}
