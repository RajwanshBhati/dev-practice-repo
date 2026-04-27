package com.interview_tracking_system.backend.dto;

/**
 * Response DTO used to show assigned interviews on the panel dashboard.
 */
public class PanelInterviewDTO {

    /**
     * Interview id used while submitting feedback.
     */
    private Long interviewId;

    /**
     * Candidate name.
     */
    private String candidateName;

    /**
     * Candidate email.
     */
    private String candidateEmail;

    /**
     * Candidate mobile number.
     */
    private String candidateMobileNumber;

    /**
     * Candidate total experience.
     */
    private Integer totalExperience;

    /**
     * Candidate relevant experience.
     */
    private Integer relevantExperience;

    /**
     * Current candidate organization.
     */
    private String currentCompany;

    /**
     * Job title for which candidate applied.
     */
    private String jobTitle;

    /**
     * Interview stage.
     */
    private String stage;

    /**
     * Scheduled interview date.
     */
    private String interviewDate;

    /**
     * Scheduled interview time.
     */
    private String interviewTime;

    /**
     * Focus areas provided by HR.
     */
    private String focusArea;

    /**
     * Candidate resume URL.
     */
    private String resumeUrl;

    /**
     * Shows whether logged-in panel has already submitted feedback.
     */
    private Boolean feedbackSubmitted;

    public Long getInterviewId() {
        return interviewId;
    }

    public void setInterviewId(final Long interviewId) {
        this.interviewId = interviewId;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(final String candidateName) {
        this.candidateName = candidateName;
    }

    public String getCandidateEmail() {
        return candidateEmail;
    }

    public void setCandidateEmail(final String candidateEmail) {
        this.candidateEmail = candidateEmail;
    }

    public String getCandidateMobileNumber() {
        return candidateMobileNumber;
    }

    public void setCandidateMobileNumber(final String candidateMobileNumber) {
        this.candidateMobileNumber = candidateMobileNumber;
    }

    public Integer getTotalExperience() {
        return totalExperience;
    }

    public void setTotalExperience(final Integer totalExperience) {
        this.totalExperience = totalExperience;
    }

    public Integer getRelevantExperience() {
        return relevantExperience;
    }

    public void setRelevantExperience(final Integer relevantExperience) {
        this.relevantExperience = relevantExperience;
    }

    public String getCurrentCompany() {
        return currentCompany;
    }

    public void setCurrentCompany(final String currentCompany) {
        this.currentCompany = currentCompany;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(final String jobTitle) {
        this.jobTitle = jobTitle;
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

    public String getFocusArea() {
        return focusArea;
    }

    public void setFocusArea(final String focusArea) {
        this.focusArea = focusArea;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setResumeUrl(final String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    public Boolean getFeedbackSubmitted() {
        return feedbackSubmitted;
    }

    public void setFeedbackSubmitted(final Boolean feedbackSubmitted) {
        this.feedbackSubmitted = feedbackSubmitted;
    }
}
