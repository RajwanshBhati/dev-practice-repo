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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

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
    @Column(unique = true, nullable = false, length = MOBILE_LENGTH, name = "mobile_number")
    private String mobile;

    /** date of birth */
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

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
    @Column(name = "current_ctc", nullable = false, precision = CTC_PRECISION, scale = CTC_SCALE)
    private BigDecimal currentCtc;

    /** Expected cost to company. */
    @Column(name = "expected_ctc", nullable = false, precision = CTC_PRECISION, scale = CTC_SCALE)
    private BigDecimal expectedCtc;

    /** Notice period in days. */
    @Column(name = "notice_period", nullable = false)
    private int noticePeriod;

    /** Preferred job location. */
    @Column(name = "preferred_location", nullable = false)
    private String preferredLocation;

    /** Source through which candidate applied (e.g. LinkedIn, Naukri, Referral). */
    @Column(name = "source")
    private String source;

    /** Current interview stage of the candidate. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Stage status;

    /** Foreign key referencing the associated job description. */
    @Column(name = "jd_id", nullable = false)
    private Long jdId;

    /**
     * Associated candidate user account (one-to-one relationship).
     */
    @OneToOne
    @JoinColumn(name = "candidate_user_id", unique = true)
    private CandidateUser candidateUser;

    /**
     * Returns the unique identifier.
     *
     * @return the candidate ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the candidate's name.
     *
     * @return the name of the candidate
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
     * @return the email address of the candidate
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the date of birth of the candidate.
     *
     * @return candidate's date of birth
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the date of birth of the candidate.
     *
     * @param dateOfBirth candidate's date of birth
     */
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Gets the source through which candidate applied.
     *
     * @return application source (e.g. LinkedIn, Naukri)
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the source through which candidate applied.
     *
     * @param source application source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Gets the associated candidate user account.
     *
     * @return candidate user entity
     */
    public CandidateUser getCandidateUser() {
        return candidateUser;
    }

    /**
     * Sets the associated candidate user account.
     *
     * @param candidateUser candidate user entity
     */
    public void setCandidateUser(CandidateUser candidateUser) {
        this.candidateUser = candidateUser;
    }

    /**
     * Sets the candidate's email.
     *
     * @param candidateEmail the email address to set
     */
    public void setEmail(final String candidateEmail) {
        this.email = candidateEmail;
    }

    /**
     * Returns the candidate's mobile number.
     *
     * @return the mobile number of the candidate
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
     * @return the URL of the candidate's resume
     */
    public String getResumeUrl() {
        return resumeUrl;
    }

    /**
     * Sets the resume URL.
     *
     * @param url the URL to set for the candidate's resume
     */
    public void setResumeUrl(final String url) {
        this.resumeUrl = url;
    }

    /**
     * Returns the current company name.
     *
     * @return the current employer of the candidate
     */
    public String getCurrentCompany() {
        return currentCompany;
    }

    /**
     * Sets the current company name.
     *
     * @param company the current employer to set for the candidate
     */
    public void setCurrentCompany(final String company) {
        this.currentCompany = company;
    }

    /**
     * Returns total years of experience.
     *
     * @return total years of work experience
     */
    public double getTotalExp() {
        return totalExp;
    }

    /**
     * Sets total years of experience.
     *
     * @param exp total years of work experience to set
     */
    public void setTotalExp(final double exp) {
        this.totalExp = exp;
    }

    /**
     * Returns relevant years of experience.
     *
     * @return relevant years of work experience
     */
    public double getRelevantExp() {
        return relevantExp;
    }

    /**
     * Sets relevant years of experience.
     *
     * @param exp relevant years of work experience to set
     */
    public void setRelevantExp(final double exp) {
        this.relevantExp = exp;
    }

    /**
     * Returns the current CTC.
     *
     * @return the current cost to company
     */
    public BigDecimal getCurrentCtc() {
        return currentCtc;
    }

    /**
     * Sets the current CTC.
     *
     * @param ctc the current cost to company to set
     */
    public void setCurrentCtc(final BigDecimal ctc) {
        this.currentCtc = ctc;
    }

    /**
     * Returns the expected CTC.
     *
     * @return the expected cost to company
     */
    public BigDecimal getExpectedCtc() {
        return expectedCtc;
    }

    /**
     * Sets the expected CTC.
     *
     * @param ctc the expected cost to company to set
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
     * @return the preferred job location
     */
    public String getPreferredLocation() {
        return preferredLocation;
    }

    /**
     * Sets the preferred location.
     *
     * @param location the preferred job location to set
     */
    public void setPreferredLocation(final String location) {
        this.preferredLocation = location;
    }

    /**
     * Returns the current interview stage.
     *
     * @return the current stage of the candidate in the interview process
     */
    public Stage getStatus() {
        return status;
    }

    /**
     * Sets the current interview stage.
     *
     * @param candidateStatus the current stage of the candidate in the interview
     *                        process to set
     */
    public void setStatus(final Stage candidateStatus) {
        this.status = candidateStatus;
    }

    /**
     * Returns the foreign key ID of the associated job description.
     *
     * @return the job description ID associated with this candidate
     */
    public Long getJdId() {
        return jdId;
    }

    /**
     * Sets the foreign key ID of the associated job description.
     *
     * @param jobDescriptionId the job description ID to associate with this
     *                         candidate
     */
    public void setJdId(final Long jobDescriptionId) {
        this.jdId = jobDescriptionId;
    }
}
