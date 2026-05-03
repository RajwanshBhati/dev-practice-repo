package com.interview_tracking_system.backend.dto;

/**
 * DTO representing complete candidate details for HR dashboard.
 */
public class HRCandidateFullDTO {

    /** Unique identifier of the candidate. */
    private Long id;

    /** Full name of the candidate. */
    private String name;

    /** Email address of the candidate. */
    private String email;

    /** Mobile number of the candidate. */
    private String mobileNumber;

    /** Current company where the candidate is working. */
    private String currentCompany;

    /** Current CTC of the candidate. */
    private Double currentCtc;

    /** Expected CTC of the candidate. */
    private Double expectedCtc;

    /** Total years of experience of the candidate. */
    private Integer totalExperience;

    /** Relevant years of experience for the applied role. */
    private Integer relevantExperience;

    /** Preferred job location of the candidate. */
    private String preferredLocation;

    /** Notice period in days. */
    private Integer noticePeriod;

    /** Source through which candidate applied. */
    private String source;

    /** Job title for which candidate applied. */
    private String jobTitle;

    /** Current hiring status of the candidate. */
    private String status;

    /** URL to access the candidate's resume. */
    private String resumeUrl;

    /**
     * Returns candidate ID.
     *
     * @return candidate ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets candidate ID.
     *
     * @param candidateId candidate ID
     */
    public void setId(final Long candidateId) {
        this.id = candidateId;
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
     * Returns candidate name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets candidate name.
     *
     * @param candidateName name
     */
    public void setName(final String candidateName) {
        this.name = candidateName;
    }

    /**
     * Returns candidate email.
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets candidate email.
     *
     * @param candidateEmail email
     */
    public void setEmail(final String candidateEmail) {
        this.email = candidateEmail;
    }

    /**
     * Returns mobile number.
     *
     * @return mobile number
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * Sets mobile number.
     *
     * @param mobile number
     */
    public void setMobileNumber(final String mobile) {
        this.mobileNumber = mobile;
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
     * @param company company name
     */
    public void setCurrentCompany(final String company) {
        this.currentCompany = company;
    }

    /**
     * Returns current CTC.
     *
     * @return current CTC
     */
    public Double getCurrentCtc() {
        return currentCtc;
    }

    /**
     * Sets current CTC.
     *
     * @param ctc current salary
     */
    public void setCurrentCtc(final Double ctc) {
        this.currentCtc = ctc;
    }

    /**
     * Returns expected CTC.
     *
     * @return expected CTC
     */
    public Double getExpectedCtc() {
        return expectedCtc;
    }

    /**
     * Sets expected CTC.
     *
     * @param ctc expected salary
     */
    public void setExpectedCtc(final Double ctc) {
        this.expectedCtc = ctc;
    }

    /**
     * Returns total experience.
     *
     * @return total experience
     */
    public Integer getTotalExperience() {
        return totalExperience;
    }

    /**
     * Sets total experience.
     *
     * @param experience total experience
     */
    public void setTotalExperience(final Integer experience) {
        this.totalExperience = experience;
    }

    /**
     * Returns relevant experience.
     *
     * @return relevant experience
     */
    public Integer getRelevantExperience() {
        return relevantExperience;
    }

    /**
     * Sets relevant experience.
     *
     * @param experience relevant experience
     */
    public void setRelevantExperience(final Integer experience) {
        this.relevantExperience = experience;
    }

    /**
     * Returns preferred location.
     *
     * @return preferred location
     */
    public String getPreferredLocation() {
        return preferredLocation;
    }

    /**
     * Sets preferred location.
     *
     * @param location preferred location
     */
    public void setPreferredLocation(final String location) {
        this.preferredLocation = location;
    }

    /**
     * Returns notice period.
     *
     * @return notice period
     */
    public Integer getNoticePeriod() {
        return noticePeriod;
    }

    /**
     * Sets notice period.
     *
     * @param notice notice period
     */
    public void setNoticePeriod(final Integer notice) {
        this.noticePeriod = notice;
    }

    /**
     * Returns source.
     *
     * @return source
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets source.
     *
     * @param applicationSource source
     */
    public void setSource(final String applicationSource) {
        this.source = applicationSource;
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
     * Returns candidate status.
     *
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets candidate status.
     *
     * @param candidateStatus status
     */
    public void setStatus(final String candidateStatus) {
        this.status = candidateStatus;
    }
}
