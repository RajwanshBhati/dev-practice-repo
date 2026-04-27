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
     * Returns the name.
     *
     * @return the candidate name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param candidateName the name to set
     */
    public void setName(final String candidateName) {
        this.name = candidateName;
    }

    /**
     * Returns the email.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email.
     *
     * @param candidateEmail the email to set
     */
    public void setEmail(final String candidateEmail) {
        this.email = candidateEmail;
    }

    /**
     * Returns the mobile country code.
     *
     * @return the mobile code
     */
    public String getMobileCode() {
        return mobileCode;
    }

    /**
     * Sets the mobile country code.
     *
     * @param code the mobile code to set
     */
    public void setMobileCode(final String code) {
        this.mobileCode = code;
    }

    /**
     * Returns the mobile number.
     *
     * @return the mobile number
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * Sets the mobile number.
     *
     * @param number the mobile number to set
     */
    public void setMobileNumber(final String number) {
        this.mobileNumber = number;
    }

    /**
     * Returns the date of birth.
     *
     * @return the date of birth
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the date of birth.
     *
     * @param dob the date of birth to set
     */
    public void setDateOfBirth(final LocalDate dob) {
        this.dateOfBirth = dob;
    }

    /**
     * Returns the current company.
     *
     * @return the current organization
     */
    public String getCurrentCompany() {
        return currentCompany;
    }

    /**
     * Sets the current company.
     *
     * @param company the current organization to set
     */
    public void setCurrentCompany(final String company) {
        this.currentCompany = company;
    }

    /**
     * Returns total experience.
     *
     * @return total years of experience
     */
    public double getTotalExp() {
        return totalExp;
    }

    /**
     * Sets total experience.
     *
     * @param exp total years of experience to set
     */
    public void setTotalExp(final double exp) {
        this.totalExp = exp;
    }

    /**
     * Returns relevant experience.
     *
     * @return relevant years of experience
     */
    public double getRelevantExp() {
        return relevantExp;
    }

    /**
     * Sets relevant experience.
     *
     * @param exp relevant years of experience to set
     */
    public void setRelevantExp(final double exp) {
        this.relevantExp = exp;
    }

    /**
     * Returns current CTC.
     *
     * @return the current cost to company
     */
    public BigDecimal getCurrentCtc() {
        return currentCtc;
    }

    /**
     * Sets current CTC.
     *
     * @param ctc the current cost to company to set
     */
    public void setCurrentCtc(final BigDecimal ctc) {
        this.currentCtc = ctc;
    }

    /**
     * Returns expected CTC.
     *
     * @return the expected cost to company
     */
    public BigDecimal getExpectedCtc() {
        return expectedCtc;
    }

    /**
     * Sets expected CTC.
     *
     * @param ctc the expected cost to company to set
     */
    public void setExpectedCtc(final BigDecimal ctc) {
        this.expectedCtc = ctc;
    }

    /**
     * Returns notice period.
     *
     * @return the notice period in days
     */
    public int getNoticePeriod() {
        return noticePeriod;
    }

    /**
     * Sets notice period.
     *
     * @param period the notice period in days to set
     */
    public void setNoticePeriod(final int period) {
        this.noticePeriod = period;
    }

    /**
     * Returns preferred location.
     *
     * @return the preferred job location
     */
    public String getPreferredLocation() {
        return preferredLocation;
    }

    /**
     * Sets preferred location.
     *
     * @param location the preferred job location to set
     */
    public void setPreferredLocation(final String location) {
        this.preferredLocation = location;
    }

    /**
     * Returns the source.
     *
     * @return the source through which candidate applied
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the source.
     *
     * @param applicationSource the source to set
     */
    public void setSource(final String applicationSource) {
        this.source = applicationSource;
    }

    /**
     * Returns the JD ID.
     *
     * @return the job description ID
     */
    public UUID getJdId() {
        return jdId;
    }

    /**
     * Sets the JD ID.
     *
     * @param jobDescriptionId the job description ID to set
     */
    public void setJdId(final UUID jobDescriptionId) {
        this.jdId = jobDescriptionId;
    }
}
