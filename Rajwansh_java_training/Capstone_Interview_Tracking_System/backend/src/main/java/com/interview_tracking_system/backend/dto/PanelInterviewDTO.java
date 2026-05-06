package com.interview_tracking_system.backend.dto;

/**
 * Response DTO used to show assigned interviews on the panel dashboard.
 */
public class PanelInterviewDTO {

    /** Interview ID used while submitting feedback. */
    private Long interviewId;

    /** Candidate name. */
    private String candidateName;

    /** Candidate email. */
    private String candidateEmail;

    /** Candidate mobile number. */
    private String candidateMobileNumber;

    /** Candidate total experience. */
    private Double totalExperience;

    /** Candidate relevant experience. */
    private Double relevantExperience;

    /** Current candidate organization. */
    private String currentCompany;

    /** Job title for which candidate applied. */
    private String jobTitle;

    /** Interview stage. */
    private String stage;

    /** Scheduled interview date. */
    private String interviewDate;

    /** Scheduled interview time. */
    private String interviewTime;

    /** Focus areas provided by HR. */
    private String focusArea;

    /** Candidate resume URL. */
    private String resumeUrl;

    /** Indicates whether feedback has been submitted. */
    private Boolean feedbackSubmitted;

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
     * Returns candidate email.
     *
     * @return candidate email
     */
    public String getCandidateEmail() {
        return candidateEmail;
    }

    /**
     * Sets candidate email.
     *
     * @param candidateEmail candidate email
     */
    public void setCandidateEmail(final String candidateEmail) {
        this.candidateEmail = candidateEmail;
    }

    /**
     * Returns candidate mobile number.
     *
     * @return mobile number
     */
    public String getCandidateMobileNumber() {
        return candidateMobileNumber;
    }

    /**
     * Sets candidate mobile number.
     *
     * @param mobileNumber mobile number
     */
    public void setCandidateMobileNumber(final String mobileNumber) {
        this.candidateMobileNumber = mobileNumber;
    }

    /**
     * Returns total experience.
     *
     * @return total experience
     */
    public Double getTotalExperience() {
        return totalExperience;
    }

    /**
     * Sets total experience.
     *
     * @param totalExperience total experience
     */
    public void setTotalExperience(final Double totalExperience) {
        this.totalExperience = totalExperience;
    }

    /**
     * Returns relevant experience.
     *
     * @return relevant experience
     */
    public Double getRelevantExperience() {
        return relevantExperience;
    }

    /**
     * Sets relevant experience.
     *
     * @param relevantExperience relevant experience
     */
    public void setRelevantExperience(final Double relevantExperience) {
        this.relevantExperience = relevantExperience;
    }

    /**
     * Returns current company.
     *
     * @return current company
     */
    public String getCurrentCompany() {
        return currentCompany;
    }

    /**
     * Sets current company.
     *
     * @param company current company
     */
    public void setCurrentCompany(final String company) {
        this.currentCompany = company;
    }

    /**
     * Returns job title.
     *
     * @return job title
     */
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     * Sets job title.
     *
     * @param title job title
     */
    public void setJobTitle(final String title) {
        this.jobTitle = title;
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
     * @param date interview date
     */
    public void setInterviewDate(final String date) {
        this.interviewDate = date;
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
     * @param time interview time
     */
    public void setInterviewTime(final String time) {
        this.interviewTime = time;
    }

    /**
     * Returns focus area.
     *
     * @return focus area
     */
    public String getFocusArea() {
        return focusArea;
    }

    /**
     * Sets focus area.
     *
     * @param focusArea focus area
     */
    public void setFocusArea(final String focusArea) {
        this.focusArea = focusArea;
    }

    /**
     * Returns resume URL.
     *
     * @return resume URL
     */
    public String getResumeUrl() {
        return resumeUrl;
    }

    /**
     * Sets resume URL.
     *
     * @param resumeUrl resume URL
     */
    public void setResumeUrl(final String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    /**
     * Returns feedback submitted status.
     *
     * @return feedback submitted
     */
    public Boolean getFeedbackSubmitted() {
        return feedbackSubmitted;
    }

    /**
     * Sets feedback submitted status.
     *
     * @param submitted feedback status
     */
    public void setFeedbackSubmitted(final Boolean submitted) {
        this.feedbackSubmitted = submitted;
    }
}
