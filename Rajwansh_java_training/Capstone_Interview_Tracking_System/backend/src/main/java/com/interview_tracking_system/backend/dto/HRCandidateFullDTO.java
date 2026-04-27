package com.interview_tracking_system.backend.dto;

/**
 * Data Transfer Object for HR dashboard candidate view.
 * Contains full candidate profile information for HR evaluation.
 */
public class HRCandidateFullDTO {

    private Long id;

    private String name;
    private String email;
    private String mobileNumber;

    private String currentCompany;
    private Double currentCtc;
    private Double expectedCtc;

    private Integer totalExperience;
    private Integer relevantExperience;

    private String preferredLocation;
    private Integer noticePeriod;
    private String source;

    private String jobTitle;
    private String status;

    /**
     * Returns candidate id.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets candidate id.
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Returns candidate name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets candidate name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCurrentCompany() {
        return currentCompany;
    }

    public void setCurrentCompany(String currentCompany) {
        this.currentCompany = currentCompany;
    }

    public Double getCurrentCtc() {
        return currentCtc;
    }

    public void setCurrentCtc(Double currentCtc) {
        this.currentCtc = currentCtc;
    }

    public Double getExpectedCtc() {
        return expectedCtc;
    }

    public void setExpectedCtc(Double expectedCtc) {
        this.expectedCtc = expectedCtc;
    }

    public Integer getTotalExperience() {
        return totalExperience;
    }

    public void setTotalExperience(Integer totalExperience) {
        this.totalExperience = totalExperience;
    }

    public Integer getRelevantExperience() {
        return relevantExperience;
    }

    public void setRelevantExperience(Integer relevantExperience) {
        this.relevantExperience = relevantExperience;
    }

    public String getPreferredLocation() {
        return preferredLocation;
    }

    public void setPreferredLocation(String preferredLocation) {
        this.preferredLocation = preferredLocation;
    }

    public Integer getNoticePeriod() {
        return noticePeriod;
    }

    public void setNoticePeriod(Integer noticePeriod) {
        this.noticePeriod = noticePeriod;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
