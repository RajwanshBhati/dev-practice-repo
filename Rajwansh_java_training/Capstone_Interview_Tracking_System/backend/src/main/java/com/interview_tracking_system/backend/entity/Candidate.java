package com.interview_tracking_system.backend.entity;

import com.interview_tracking_system.backend.enums.Stage;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;

/**
 * Represents a candidate in the interview tracking system.
 */
@Entity
@Table(name = "candidates", indexes = {
        @Index(name = "idx_email", columnList = "email"),
        @Index(name = "idx_mobile", columnList = "mobile_number")
})
public final class Candidate {

    /** Maximum length for mobile number field. */
    private static final int MOBILE_LENGTH = 15;

    /** Precision for CTC decimal fields. */
    private static final int CTC_PRECISION = 12;

    /** Scale for CTC decimal fields. */
    private static final int CTC_SCALE = 2;

    /**
     * Unique identifier for the candidate.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Full name of the candidate. */
    private String name;

    /** Unique email address of the candidate. */
    @Column(unique = true)
    private String email;

    /** Unique mobile number of the candidate. */
    @Column(unique = true, nullable = false,
            length = MOBILE_LENGTH, name = "mobile_number")
    private String mobile;

    /** URL pointing to the candidate's resume. */
    @Column(name = "resume_url", nullable = false)
    private String resumeUrl;

    /** Current employer of the candidate. */
    @Column(name = "current_company")
    private String currentCompany;

    /** Total years of work experience. */
    @Column(name = "total_experience", nullable = false)
    private double totalExp;

    /** Relevant years of work experience. */
    @Column(name = "relevant_experience", nullable = false)
    private double relevantExp;

    /** Current cost to company. */
    @Column(name = "current_ctc", nullable = false,
            precision = CTC_PRECISION, scale = CTC_SCALE)
    private BigDecimal currentCtc;

    /** Expected cost to company. */
    @Column(name = "expected_ctc", nullable = false,
            precision = CTC_PRECISION, scale = CTC_SCALE)
    private BigDecimal expectedCtc;

    /** Notice period in days. */
    @Column(name = "notice_period", nullable = false)
    private int noticePeriod;

    /** Preferred job location. */
    @Column(name = "preferred_location", nullable = false)
    private String preferredLocation;

    /** Current interview stage of the candidate. */
    @Enumerated(EnumType.STRING)
    private Stage status;

    /** Job description the candidate applied for. */
    @ManyToOne
    @JoinColumn(name = "jd_id")
    private JobDescription jobDescription;

    /**
     * Default constructor required by JPA.
     */
    public Candidate() {
    }

    /**
     * Returns the unique identifier.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the candidate's name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the candidate's name.
     *
     * @param candidateName the name to set
     */
    public void setName(final String candidateName) {
        this.name = candidateName;
    }

    /**
     * Returns the candidate's email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the candidate's email.
     *
     * @param candidateEmail the email to set
     */
    public void setEmail(final String candidateEmail) {
        this.email = candidateEmail;
    }

    /**
     * Returns the candidate's mobile number.
     *
     * @return the mobile number
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Sets the candidate's mobile number.
     *
     * @param candidateMobile the mobile number to set
     */
    public void setMobile(final String candidateMobile) {
        this.mobile = candidateMobile;
    }

    /**
     * Returns the resume URL.
     *
     * @return the resume URL
     */
    public String getResumeUrl() {
        return resumeUrl;
    }

    /**
     * Sets the resume URL.
     *
     * @param url the resume URL to set
     */
    public void setResumeUrl(final String url) {
        this.resumeUrl = url;
    }

    /**
     * Returns the current company name.
     *
     * @return the current company
     */
    public String getCurrentCompany() {
        return currentCompany;
    }

    /**
     * Sets the current company name.
     *
     * @param company the current company to set
     */
    public void setCurrentCompany(final String company) {
        this.currentCompany = company;
    }

    /**
     * Returns total years of experience.
     *
     * @return total experience
     */
    public double getTotalExp() {
        return totalExp;
    }

    /**
     * Sets total years of experience.
     *
     * @param exp the total experience to set
     */
    public void setTotalExp(final double exp) {
        this.totalExp = exp;
    }

    /**
     * Returns relevant years of experience.
     *
     * @return relevant experience
     */
    public double getRelevantExp() {
        return relevantExp;
    }

    /**
     * Sets relevant years of experience.
     *
     * @param exp the relevant experience to set
     */
    public void setRelevantExp(final double exp) {
        this.relevantExp = exp;
    }

    /**
     * Returns the current CTC.
     *
     * @return the current CTC
     */
    public BigDecimal getCurrentCtc() {
        return currentCtc;
    }

    /**
     * Sets the current CTC.
     *
     * @param ctc the current CTC to set
     */
    public void setCurrentCtc(final BigDecimal ctc) {
        this.currentCtc = ctc;
    }

    /**
     * Returns the expected CTC.
     *
     * @return the expected CTC
     */
    public BigDecimal getExpectedCtc() {
        return expectedCtc;
    }

    /**
     * Sets the expected CTC.
     *
     * @param ctc the expected CTC to set
     */
    public void setExpectedCtc(final BigDecimal ctc) {
        this.expectedCtc = ctc;
    }

    /**
     * Returns the notice period in days.
     *
     * @return the notice period
     */
    public int getNoticePeriod() {
        return noticePeriod;
    }

    /**
     * Sets the notice period in days.
     *
     * @param period the notice period to set
     */
    public void setNoticePeriod(final int period) {
        this.noticePeriod = period;
    }

    /**
     * Returns the preferred location.
     *
     * @return the preferred location
     */
    public String getPreferredLocation() {
        return preferredLocation;
    }

    /**
     * Sets the preferred location.
     *
     * @param location the preferred location to set
     */
    public void setPreferredLocation(final String location) {
        this.preferredLocation = location;
    }

    /**
     * Returns the current interview stage.
     *
     * @return the status
     */
    public Stage getStatus() {
        return status;
    }

    /**
     * Sets the current interview stage.
     *
     * @param candidateStatus the status to set
     */
    public void setStatus(final Stage candidateStatus) {
        this.status = candidateStatus;
    }

    /**
     * Returns the associated job description.
     *
     * @return the job description
     */
    public JobDescription getJobDescription() {
        return jobDescription;
    }

    /**
     * Sets the associated job description.
     *
     * @param jd the job description to set
     */
    public void setJobDescription(final JobDescription jd) {
        this.jobDescription = jd;
    }
}