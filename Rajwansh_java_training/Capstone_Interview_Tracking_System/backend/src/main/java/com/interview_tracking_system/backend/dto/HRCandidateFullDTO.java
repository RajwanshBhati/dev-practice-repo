package com.interview_tracking_system.backend.dto;

public class HRCandidateFullDTO {

    /** Unique identifier of the candidate */
    private Long id;

    /** Full name of the candidate */
    private String name;

    /** Email address of the candidate */
    private String email;

    /** Mobile number of the candidate */
    private String mobileNumber;

    /** Current company where the candidate is working */
    private String currentCompany;

    /** Current CTC of the candidate */
    private Double currentCtc;

    /** Expected CTC of the candidate */
    private Double expectedCtc;

    /** Total years of experience of the candidate */
    private Integer totalExperience;

    /** Relevant years of experience for the applied role */
    private Integer relevantExperience;

    /** Preferred job location of the candidate */
    private String preferredLocation;

    /** Notice period in days */
    private Integer noticePeriod;

    /** Source through which candidate applied */
    private String source;

    /** Job title for which candidate applied */
    private String jobTitle;

    /** Current hiring status of the candidate */
    private String status;

    /** URL to access the candidate's resume */
    private String resumeUrl;

    /**
     * Returns candidate id
     * 
     * @return candidate id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets candidate id
     * 
     * @param id candidate id
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Returns resume URL
     * 
     * @return resume URL
     */
    public String getResumeUrl() {
        return resumeUrl;
    }

    /**
     * Sets resume URL
     * 
     * @param resumeUrl resume URL
     */
    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    /**
     * Returns candidate name
     * 
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets candidate name
     * 
     * @param name candidate name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns candidate email
     * 
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets candidate email
     * 
     * @param email email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns candidate mobile number
     * 
     * @return mobile number
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * Sets candidate mobile number
     * 
     * @param mobileNumber mobile number
     */
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    /**
     * Returns current company
     * 
     * @return current company
     */
    public String getCurrentCompany() {
        return currentCompany;
    }

    /**
     * Sets current company
     * 
     * @param currentCompany company name
     */
    public void setCurrentCompany(String currentCompany) {
        this.currentCompany = currentCompany;
    }

    /**
     * Returns current CTC
     * 
     * @return current CTC
     */
    public Double getCurrentCtc() {
        return currentCtc;
    }

    /**
     * Sets current CTC
     * 
     * @param currentCtc current salary
     */
    public void setCurrentCtc(Double currentCtc) {
        this.currentCtc = currentCtc;
    }

    /**
     * Returns expected CTC
     * 
     * @return expected CTC
     */
    public Double getExpectedCtc() {
        return expectedCtc;
    }

    /**
     * Sets expected CTC
     * 
     * @param expectedCtc expected salary
     */
    public void setExpectedCtc(Double expectedCtc) {
        this.expectedCtc = expectedCtc;
    }

    /**
     * Returns total experience
     * 
     * @return total experience
     */
    public Integer getTotalExperience() {
        return totalExperience;
    }

    /**
     * Sets total experience
     * 
     * @param totalExperience experience in years
     */
    public void setTotalExperience(Integer totalExperience) {
        this.totalExperience = totalExperience;
    }

    /**
     * Returns relevant experience
     * 
     * @return relevant experience
     */
    public Integer getRelevantExperience() {
        return relevantExperience;
    }

    /**
     * Sets relevant experience
     * 
     * @param relevantExperience relevant years
     */
    public void setRelevantExperience(Integer relevantExperience) {
        this.relevantExperience = relevantExperience;
    }

    /**
     * Returns preferred location
     * 
     * @return location
     */
    public String getPreferredLocation() {
        return preferredLocation;
    }

    /**
     * Sets preferred location
     * 
     * @param preferredLocation location
     */
    public void setPreferredLocation(String preferredLocation) {
        this.preferredLocation = preferredLocation;
    }

    /**
     * Returns notice period
     * 
     * @return notice period in days
     */
    public Integer getNoticePeriod() {
        return noticePeriod;
    }

    /**
     * Sets notice period
     * 
     * @param noticePeriod days
     */
    public void setNoticePeriod(Integer noticePeriod) {
        this.noticePeriod = noticePeriod;
    }

    /**
     * Returns source
     * 
     * @return source
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets source
     * 
     * @param source source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Returns job title
     * 
     * @return job title
     */
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     * Sets job title
     * 
     * @param jobTitle job title
     */
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    /**
     * Returns candidate status
     * 
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets candidate status
     * 
     * @param status status
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
