package com.interview_tracking_system.backend.entity;

import com.interview_tracking_system.backend.enums.Stage;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

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
import java.util.UUID;

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

    /** Unique identifier for the candidate. */
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

    /** Date of birth. */
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
    @Column(name = "current_ctc", nullable = true, precision = CTC_PRECISION, scale = CTC_SCALE)
    private BigDecimal currentCtc;

    /** Expected cost to company. */
    @Column(name = "expected_ctc", nullable = true, precision = CTC_PRECISION, scale = CTC_SCALE)
    private BigDecimal expectedCtc;

    /** Notice period in days. */
    @Column(name = "notice_period", nullable = false)
    private int noticePeriod;

    /** Preferred job location. */
    @Column(name = "preferred_location", nullable = false)
    private String preferredLocation;

    /** Source through which candidate applied. */
    @Column(name = "source")
    private String source;

    /** Current interview stage of the candidate. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Stage status;

    /** Foreign key referencing the associated job description. */
    @Column(name = "jd_id", nullable = true)
    private UUID jdId;

    /** Associated candidate user account. */
    @OneToOne
    @JoinColumn(name = "candidate_user_id", unique = true)
    private CandidateUser candidateUser;

    /** Stage where candidate was rejected. */
    @Enumerated(EnumType.STRING)
    @Column(name = "rejected_stage")
    private Stage rejectedStage;

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
     * Returns candidate name.
     *
     * @return candidate name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets candidate name.
     *
     * @param candidateName candidate name
     */
    public void setName(final String candidateName) {
        this.name = candidateName;
    }

    /**
     * Returns candidate email.
     *
     * @return candidate email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets candidate email.
     *
     * @param candidateEmail candidate email
     */
    public void setEmail(final String candidateEmail) {
        this.email = candidateEmail;
    }

    /**
     * Returns candidate mobile number.
     *
     * @return mobile number
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Sets candidate mobile number.
     *
     * @param candidateMobile mobile number
     */
    public void setMobile(final String candidateMobile) {
        this.mobile = candidateMobile;
    }

    /**
     * Returns date of birth.
     *
     * @return date of birth
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets date of birth.
     *
     * @param dob date of birth
     */
    public void setDateOfBirth(final LocalDate dob) {
        this.dateOfBirth = dob;
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
     * @param url resume URL
     */
    public void setResumeUrl(final String url) {
        this.resumeUrl = url;
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
     * @param company current company
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
     * @param exp total experience
     */
    public void setTotalExp(final double exp) {
        this.totalExp = exp;
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
     * @param exp relevant experience
     */
    public void setRelevantExp(final double exp) {
        this.relevantExp = exp;
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
     * @param period notice period
     */
    public void setNoticePeriod(final int period) {
        this.noticePeriod = period;
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
     * @param candidateSource source
     */
    public void setSource(final String candidateSource) {
        this.source = candidateSource;
    }

    /**
     * Returns current candidate status.
     *
     * @return status
     */
    public Stage getStatus() {
        return status;
    }

    /**
     * Sets current candidate status.
     *
     * @param candidateStatus status
     */
    public void setStatus(final Stage candidateStatus) {
        this.status = candidateStatus;
    }

    /**
     * Returns job description ID.
     *
     * @return job description ID
     */
    public UUID getJdId() {
        return jdId;
    }

    /**
     * Sets job description ID.
     *
     * @param jobDescriptionId job description ID
     */
    public void setJdId(final UUID jobDescriptionId) {
        this.jdId = jobDescriptionId;
    }

    /**
     * Returns associated candidate user.
     *
     * @return candidate user
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "JPA relationship entity reference is intentionally returned.")
    public CandidateUser getCandidateUser() {
        return candidateUser;
    }

    /**
     * Sets associated candidate user.
     *
     * @param user candidate user
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "JPA relationship entity reference is intentionally stored.")
    public void setCandidateUser(final CandidateUser user) {
        this.candidateUser = user;
    }

    /**
     * Returns rejected stage.
     *
     * @return rejected stage
     */
    public Stage getRejectedStage() {
        return rejectedStage;
    }

    /**
     * Sets rejected stage.
     *
     * @param stage rejected stage
     */
    public void setRejectedStage(final Stage stage) {
        this.rejectedStage = stage;
    }
}
