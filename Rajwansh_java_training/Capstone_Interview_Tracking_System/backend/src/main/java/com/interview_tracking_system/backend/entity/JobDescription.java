package com.interview_tracking_system.backend.entity;

import com.interview_tracking_system.backend.enums.JDStatus;
import com.interview_tracking_system.backend.enums.JobType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Represents a Job Description created by HR.
 */
@Entity
@Table(name = "job_descriptions")
public class JobDescription {

    /** Precision for salary decimal fields. */
    private static final int SALARY_PRECISION = 12;

    /** Scale for salary decimal fields. */
    private static final int SALARY_SCALE = 2;

    /** Unique identifier for JD. */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** Job title like Java Developer, Data Engineer. */
    @Column(nullable = false)
    private String jobTitle;

    /** Detailed job description text. */
    @Column(columnDefinition = "TEXT")
    private String jobDescription;

    /** Required skills list. */
    @ElementCollection
    @CollectionTable(
        name = "jd_skills",
        joinColumns = @JoinColumn(name = "jd_id")
    )
    @Column(name = "skill")
    private List<String> skillsRequired;

    /** Minimum experience required in years. */
    @Column(name = "experience_min", nullable = false)
    private Integer experienceMin;

    /** Maximum experience required in years. */
    @Column(name = "experience_max", nullable = false)
    private Integer experienceMax;

    /** Minimum salary offered. */
    @Column(name = "salary_min", nullable = false,
            precision = SALARY_PRECISION, scale = SALARY_SCALE)
    private BigDecimal salaryMin;

    /** Maximum salary offered. */
    @Column(name = "salary_max", nullable = false,
            precision = SALARY_PRECISION, scale = SALARY_SCALE)
    private BigDecimal salaryMax;

    /** Job location. */
    @Column(nullable = false)
    private String location;

    /** Job type such as FULL_TIME, CONTRACT, or REMOTE. */
    @Enumerated(EnumType.STRING)
    private JobType jobType;

    /** Current status of the JD. */
    @Enumerated(EnumType.STRING)
    private JDStatus status = JDStatus.ACTIVE;

    /** HR user who created this JD. */
    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    /**
     * Default constructor for JPA.
     */
    public JobDescription() {
    }

    /**
     * Returns the unique identifier.
     *
     * @return the id
     */
    public UUID getId() {
        return id;
    }

    /**
     * Returns the job title.
     *
     * @return the job title
     */
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     * Sets the job title.
     *
     * @param title the job title to set
     */
    public void setJobTitle(final String title) {
        this.jobTitle = title;
    }

    /**
     * Returns the job description text.
     *
     * @return the job description
     */
    public String getJobDescription() {
        return jobDescription;
    }

    /**
     * Sets the job description text.
     *
     * @param description the description to set
     */
    public void setJobDescription(final String description) {
        this.jobDescription = description;
    }

    /**
     * Returns the list of required skills.
     *
     * @return the skills list
     */
    public List<String> getSkillsRequired() {
        return skillsRequired;
    }

    /**
     * Sets the list of required skills.
     *
     * @param skills the skills list to set
     */
    public void setSkillsRequired(final List<String> skills) {
        this.skillsRequired = skills;
    }

    /**
     * Returns the minimum experience required.
     *
     * @return minimum experience in years
     */
    public Integer getExperienceMin() {
        return experienceMin;
    }

    /**
     * Sets the minimum experience required.
     *
     * @param minExp the minimum experience to set
     */
    public void setExperienceMin(final Integer minExp) {
        this.experienceMin = minExp;
    }

    /**
     * Returns the maximum experience required.
     *
     * @return maximum experience in years
     */
    public Integer getExperienceMax() {
        return experienceMax;
    }

    /**
     * Sets the maximum experience required.
     *
     * @param maxExp the maximum experience to set
     */
    public void setExperienceMax(final Integer maxExp) {
        this.experienceMax = maxExp;
    }

    /**
     * Returns the minimum salary.
     *
     * @return the minimum salary
     */
    public BigDecimal getSalaryMin() {
        return salaryMin;
    }

    /**
     * Sets the minimum salary.
     *
     * @param minSalary the minimum salary to set
     */
    public void setSalaryMin(final BigDecimal minSalary) {
        this.salaryMin = minSalary;
    }

    /**
     * Returns the maximum salary.
     *
     * @return the maximum salary
     */
    public BigDecimal getSalaryMax() {
        return salaryMax;
    }

    /**
     * Sets the maximum salary.
     *
     * @param maxSalary the maximum salary to set
     */
    public void setSalaryMax(final BigDecimal maxSalary) {
        this.salaryMax = maxSalary;
    }

    /**
     * Returns the job location.
     *
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the job location.
     *
     * @param jobLocation the location to set
     */
    public void setLocation(final String jobLocation) {
        this.location = jobLocation;
    }

    /**
     * Returns the job type.
     *
     * @return the job type
     */
    public JobType getJobType() {
        return jobType;
    }

    /**
     * Sets the job type.
     *
     * @param type the job type to set
     */
    public void setJobType(final JobType type) {
        this.jobType = type;
    }

    /**
     * Returns the JD status.
     *
     * @return the status
     */
    public JDStatus getStatus() {
        return status;
    }

    /**
     * Returns the HR user who created this JD.
     *
     * @return the creator
     */
    public User getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the HR user who created this JD.
     *
     * @param creator the HR user to set
     */
    public void setCreatedBy(final User creator) {
        this.createdBy = creator;
    }
}