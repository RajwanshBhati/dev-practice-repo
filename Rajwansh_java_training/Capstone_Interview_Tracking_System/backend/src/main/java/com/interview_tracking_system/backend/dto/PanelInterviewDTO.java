package com.interview_tracking_system.backend.dto;

/**
 * Response DTO used to show assigned interviews on the panel dashboard
 */
public class PanelInterviewDTO {

    /**
     * Interview id used while submitting feedback
     */
    private Long interviewId;

    /**
     * Candidate name
     */
    private String candidateName;

    /**
     * Candidate email
     */
    private String candidateEmail;

    /**
     * Candidate mobile number
     */
    private String candidateMobileNumber;

    /**
     * Candidate total experience
     */
    private Double totalExperience;

    /**
     * Candidate relevant experience
     */
    private Double relevantExperience;

    /**
     * Current candidate organization
     */
    private String currentCompany;

    /**
     * Job title for which candidate applied
     */
    private String jobTitle;

    /**
     * Interview stage
     */
    private String stage;

    /**
     * Scheduled interview date
     */
    private String interviewDate;

    /**
     * Scheduled interview time
     */
    private String interviewTime;

    /**
     * Focus areas provided by HR
     */
    private String focusArea;

    /**
     * Candidate resume URL
     */
    private String resumeUrl;

    /**
     * Indicates whether feedback has been submitted by the panel
     */
    private Boolean feedbackSubmitted;

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
     * Returns candidate email
     */
    public String getCandidateEmail() {
        return candidateEmail;
    }

    /**
     * Sets candidate email
     */
    public void setCandidateEmail(final String candidateEmail) {
        this.candidateEmail = candidateEmail;
    }

    /**
     * Returns candidate mobile number
     */
    public String getCandidateMobileNumber() {
        return candidateMobileNumber;
    }

    /**
     * Sets candidate mobile number
     */
    public void setCandidateMobileNumber(final String candidateMobileNumber) {
        this.candidateMobileNumber = candidateMobileNumber;
    }

    /**
     * Returns total experience
     */
    public Double getTotalExperience() {
        return totalExperience;
    }

    /**
     * Sets total experience
     */
    public void setTotalExperience(final Double totalExperience) {
        this.totalExperience = totalExperience;
    }

    /**
     * Returns relevant experience
     */
    public Double getRelevantExperience() {
        return relevantExperience;
    }

    /**
     * Sets relevant experience
     */
    public void setRelevantExperience(final Double relevantExperience) {
        this.relevantExperience = relevantExperience;
    }

    /**
     * Returns current company
     */
    public String getCurrentCompany() {
        return currentCompany;
    }

    /**
     * Sets current company
     */
    public void setCurrentCompany(final String currentCompany) {
        this.currentCompany = currentCompany;
    }

    /**
     * Returns job title
     */
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     * Sets job title
     */
    public void setJobTitle(final String jobTitle) {
        this.jobTitle = jobTitle;
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
     * Returns focus area
     */
    public String getFocusArea() {
        return focusArea;
    }

    /**
     * Sets focus area
     */
    public void setFocusArea(final String focusArea) {
        this.focusArea = focusArea;
    }

    /**
     * Returns resume URL
     */
    public String getResumeUrl() {
        return resumeUrl;
    }

    /**
     * Sets resume URL
     */
    public void setResumeUrl(final String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    /**
     * Returns feedback submitted status
     */
    public Boolean getFeedbackSubmitted() {
        return feedbackSubmitted;
    }

    /**
     * Sets feedback submitted status
     */
    public void setFeedbackSubmitted(final Boolean feedbackSubmitted) {
        this.feedbackSubmitted = feedbackSubmitted;
    }
}
