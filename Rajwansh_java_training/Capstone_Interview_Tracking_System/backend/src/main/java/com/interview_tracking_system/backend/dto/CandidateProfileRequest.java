package com.interview_tracking_system.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for candidate profiling/apply request.
 */
public class CandidateProfileRequest {

    /** Full name of the candidate. */
    private String name;

    /** Email address of the candidate. */
    private String email;

    /** Mobile country code. */
    private String mobileCode;

    /** Mobile number of the candidate. */
    private String mobileNumber;

    /** Date of birth (optional). */
    private LocalDate dateOfBirth;

    /** Current organization of the candidate. */
    private String currentCompany;

    /** Total years of experience. */
    private double totalExp;

    /** Relevant years of experience. */
    private double relevantExp;

    /** Current cost to company. */
    private BigDecimal currentCtc;

    /** Expected cost to company. */
    private BigDecimal expectedCtc;

    /** Notice period in days. */
    private int noticePeriod;

    /** Preferred job location. */
    private String preferredLocation;

    /** Source through which candidate applied. */
    private String source;

    /** Job description ID candidate is applying for. */
    private UUID jdId;

    /**
     * Returns the candidate name.
     *
     * @return candidate name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the candidate name.
     *
     * @param candidateName name to set
     */
    public void setName(final String candidateName) {
        this.name = candidateName;
    }

    /**
     * Returns the email address.
     *
     * @return email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address.
     *
     * @param candidateEmail email to set
     */
    public void setEmail(final String candidateEmail) {
        this.email = candidateEmail;
    }

    /**
     * Returns the mobile country code.
     *
     * @return mobile code
     */
    public String getMobileCode() {
        return mobileCode;
    }

    /**
     * Sets the mobile country code.
     *
     * @param mobileCountryCode code to set
     */
    public void setMobileCode(final String mobileCountryCode) {
        this.mobileCode = mobileCountryCode;
    }

    /**
     * Returns the mobile number.
     *
     * @return mobile number
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * Sets the mobile number.
     *
     * @param mobile number to set
     */
    public void setMobileNumber(final String mobile) {
        this.mobileNumber = mobile;
    }

    /**
     * Returns the date of birth.
     *
     * @return date of birth
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the date of birth.
     *
     * @param dob date of birth
     */
    public void setDateOfBirth(final LocalDate dob) {
        this.dateOfBirth = dob;
    }

    /**
     * Returns the current company.
     *
     * @return current company
     */
    public String getCurrentCompany() {
        return currentCompany;
    }

    /**
     * Sets the current company.
     *
     * @param company company to set
     */
    public void setCurrentCompany(final String company) {
        this.currentCompany = company;
    }

    /**
     * Returns total experience.
     *
     * @return total experience
     */
    public double getTotalExp() {
        return totalExp;
    }

    /**
     * Sets total experience.
     *
     * @param experience total experience
     */
    public void setTotalExp(final double experience) {
        this.totalExp = experience;
    }

    /**
     * Returns relevant experience.
     *
     * @return relevant experience
     */
    public double getRelevantExp() {
        return relevantExp;
    }

    /**
     * Sets relevant experience.
     *
     * @param experience relevant experience
     */
    public void setRelevantExp(final double experience) {
        this.relevantExp = experience;
    }

    /**
     * Returns current CTC.
     *
     * @return current CTC
     */
    public BigDecimal getCurrentCtc() {
        return currentCtc;
    }

    /**
     * Sets current CTC.
     *
     * @param ctc current CTC
     */
    public void setCurrentCtc(final BigDecimal ctc) {
        this.currentCtc = ctc;
    }

    /**
     * Returns expected CTC.
     *
     * @return expected CTC
     */
    public BigDecimal getExpectedCtc() {
        return expectedCtc;
    }

    /**
     * Sets expected CTC.
     *
     * @param ctc expected CTC
     */
    public void setExpectedCtc(final BigDecimal ctc) {
        this.expectedCtc = ctc;
    }

    /**
     * Returns notice period.
     *
     * @return notice period
     */
    public int getNoticePeriod() {
        return noticePeriod;
    }

    /**
     * Sets notice period.
     *
     * @param notice notice period
     */
    public void setNoticePeriod(final int notice) {
        this.noticePeriod = notice;
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
     * Returns application source.
     *
     * @return source
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets application source.
     *
     * @param applicationSource source
     */
    public void setSource(final String applicationSource) {
        this.source = applicationSource;
    }

    /**
     * Returns job description ID.
     *
     * @return JD ID
     */
    public UUID getJdId() {
        return jdId;
    }

    /**
     * Sets job description ID.
     *
     * @param jobDescriptionId JD ID
     */
    public void setJdId(final UUID jobDescriptionId) {
        this.jdId = jobDescriptionId;
    }
}
