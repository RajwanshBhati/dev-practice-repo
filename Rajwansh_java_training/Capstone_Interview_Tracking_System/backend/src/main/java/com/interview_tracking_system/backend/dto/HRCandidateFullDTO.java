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
     * Returns candidate id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets candidate id
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Returns candidate name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets candidate name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns candidate email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets candidate email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns candidate mobile number
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * Sets candidate mobile number
     */
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    /**
     * Returns current company of candidate
     */
    public String getCurrentCompany() {
        return currentCompany;
    }

    /**
     * Sets current company of candidate
     */
    public void setCurrentCompany(String currentCompany) {
        this.currentCompany = currentCompany;
    }

    /**
     * Returns current CTC of candidate
     */
    public Double getCurrentCtc() {
        return currentCtc;
    }

    /**
     * Sets current CTC of candidate
     */
    public void setCurrentCtc(Double currentCtc) {
        this.currentCtc = currentCtc;
    }

    /**
     * Returns expected CTC of candidate
     */
    public Double getExpectedCtc() {
        return expectedCtc;
    }

    /**
     * Sets expected CTC of candidate
     */
    public void setExpectedCtc(Double expectedCtc) {
        this.expectedCtc = expectedCtc;
    }

    /**
     * Returns total experience of candidate
     */
    public Integer getTotalExperience() {
        return totalExperience;
    }

    /**
     * Sets total experience of candidate
     */
    public void setTotalExperience(Integer totalExperience) {
        this.totalExperience = totalExperience;
    }

    /**
     * Returns relevant experience of candidate
     */
    public Integer getRelevantExperience() {
        return relevantExperience;
    }

    /**
     * Sets relevant experience of candidate
     */
    public void setRelevantExperience(Integer relevantExperience) {
        this.relevantExperience = relevantExperience;
    }

    /**
     * Returns preferred location of candidate
     */
    public String getPreferredLocation() {
        return preferredLocation;
    }

    /**
     * Sets preferred location of candidate
     */
    public void setPreferredLocation(String preferredLocation) {
        this.preferredLocation = preferredLocation;
    }

    /**
     * Returns notice period of candidate
     */
    public Integer getNoticePeriod() {
        return noticePeriod;
    }

    /**
     * Sets notice period of candidate
     */
    public void setNoticePeriod(Integer noticePeriod) {
        this.noticePeriod = noticePeriod;
    }

    /**
     * Returns source of candidate
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets source of candidate
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Returns job title applied by candidate
     */
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     * Sets job title applied by candidate
     */
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    /**
     * Returns current status of candidate
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets current status of candidate
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
