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
public class Candidate {

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
    @Column(nullable = false)
    private String name;

    /** Unique email address of the candidate. */
    @Column(unique = true, nullable = false)
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
    @Column(nullable = false)
    private Stage status;

    /** Foreign key referencing the associated job description. */
    @Column(name = "jd_id", nullable = false)
    private Long jdId;

    /**
     * Default constructor required by JPA.
     */
    public Candidate() {
    }

    /**
     * Returns the unique identifier.
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the candidate's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the candidate's name.
     */
    public void setName(final String candidateName) {
        this.name = candidateName;
    }

    /**
     * Returns the candidate's email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the candidate's email.
     */
    public void setEmail(final String candidateEmail) {
        this.email = candidateEmail;
    }

    /**
     * Returns the candidate's mobile number.
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Sets the candidate's mobile number.
     */
    public void setMobile(final String candidateMobile) {
        this.mobile = candidateMobile;
    }

    /**
     * Returns the resume URL.
     */
    public String getResumeUrl() {
        return resumeUrl;
    }

    /**
     * Sets the resume URL.
     */
    public void setResumeUrl(final String url) {
        this.resumeUrl = url;
    }

    /**
     * Returns the current company name.
     */
    public String getCurrentCompany() {
        return currentCompany;
    }

    /**
     * Sets the current company name.
     */
    public void setCurrentCompany(final String company) {
        this.currentCompany = company;
    }

    /**
     * Returns total years of experience.
     */
    public double getTotalExp() {
        return totalExp;
    }

    /**
     * Sets total years of experience.
     */
    public void setTotalExp(final double exp) {
        this.totalExp = exp;
    }

    /**
     * Returns relevant years of experience.
     */
    public double getRelevantExp() {
        return relevantExp;
    }

    /**
     * Sets relevant years of experience.
     */
    public void setRelevantExp(final double exp) {
        this.relevantExp = exp;
    }

    /**
     * Returns the current CTC.
     */
    public BigDecimal getCurrentCtc() {
        return currentCtc;
    }

    /**
     * Sets the current CTC.
     */
    public void setCurrentCtc(final BigDecimal ctc) {
        this.currentCtc = ctc;
    }

    /**
     * Returns the expected CTC.
     */
    public BigDecimal getExpectedCtc() {
        return expectedCtc;
    }

    /**
     * Sets the expected CTC.
     */
    public void setExpectedCtc(final BigDecimal ctc) {
        this.expectedCtc = ctc;
    }

    /**
     * Returns the notice period in days.
     */
    public int getNoticePeriod() {
        return noticePeriod;
    }

    /**
     * Sets the notice period in days.
     */
    public void setNoticePeriod(final int period) {
        this.noticePeriod = period;
    }

    /**
     * Returns the preferred location.
     */
    public String getPreferredLocation() {
        return preferredLocation;
    }

    /**
     * Sets the preferred location.
     */
    public void setPreferredLocation(final String location) {
        this.preferredLocation = location;
    }

    /**
     * Returns the current interview stage.
     */
    public Stage getStatus() {
        return status;
    }

    /**
     * Sets the current interview stage.
     */
    public void setStatus(final Stage candidateStatus) {
        this.status = candidateStatus;
    }

    /**
     * Returns the foreign key ID of the associated job description.
     */
    public Long getJdId() {
        return jdId;
    }

    /**
     * Sets the foreign key ID of the associated job description.
     */
    public void setJdId(final Long jobDescriptionId) {
        this.jdId = jobDescriptionId;
    }
}