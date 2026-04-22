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
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
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

    /** Unique identifier for the job description. */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** Job title such as Java Developer or Data Engineer. */
    @Column(nullable = false)
    private String jobTitle;

    /** Detailed job description text. */
    @Column(columnDefinition = "TEXT")
    private String jobDescription;

    /** Required skills list stored in a separate table. */
    @ElementCollection
    @CollectionTable(name = "jd_skills", joinColumns = @JoinColumn(name = "jd_id"))
    @Column(name = "skill")
    private List<String> skillsRequired;

    /** Minimum experience required in years. */
    @Column(name = "experience_min", nullable = false)
    private Integer experienceMin;

    /** Maximum experience required in years. */
    @Column(name = "experience_max", nullable = false)
    private Integer experienceMax;

    /** Minimum salary offered. */
    @Column(name = "salary_min", nullable = false, precision = SALARY_PRECISION, scale = SALARY_SCALE)
    private BigDecimal salaryMin;

    /** Maximum salary offered. */
    @Column(name = "salary_max", nullable = false, precision = SALARY_PRECISION, scale = SALARY_SCALE)
    private BigDecimal salaryMax;

    /** Job location. */
    @Column(nullable = false)
    private String location;

    /** Job type such as FULL_TIME, CONTRACT, or REMOTE. */
    @Enumerated(EnumType.STRING)
    private JobType jobType;

    /** Current status of the job description. */
    @Enumerated(EnumType.STRING)
    private JDStatus status = JDStatus.ACTIVE;

    /** Foreign key referencing the HR user who created this JD. */
    @Column(name = "created_by", nullable = false)
    private Long createdById;

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
     * @return the jobTitle
     */
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     * Sets the job title.
     *
     * @param title the jobTitle to set
     */
    public void setJobTitle(final String title) {
        this.jobTitle = title;
    }

    /**
     * Returns the job description text.
     *
     * @return the jobDescription
     */
    public String getJobDescription() {
        return jobDescription;
    }

    /**
     * Sets the job description text.
     *
     * @param description the jobDescription to set
     */
    public void setJobDescription(final String description) {
        this.jobDescription = description;
    }

    /**
     * Returns the list of required skills.
     *
     * @return the skillsRequired
     */
    public List<String> getSkillsRequired() {
        return skillsRequired == null ? null : new ArrayList<>(skillsRequired);
    }

    /**
     * Sets the list of required skills.
     *
     * @param skills the skillsRequired to set
     */
    public void setSkillsRequired(final List<String> skills) {
        this.skillsRequired = skills == null ? null : new ArrayList<>(skills);
    }

    /**
     * Returns the minimum experience required.
     *
     * @return the experienceMin
     */
    public Integer getExperienceMin() {
        return experienceMin;
    }

    /**
     * Sets the minimum experience required.
     *
     * @param minExp the experienceMin to set
     */
    public void setExperienceMin(final Integer minExp) {
        this.experienceMin = minExp;
    }

    /**
     * Returns the maximum experience required.
     *
     * @return the experienceMax
     */
    public Integer getExperienceMax() {
        return experienceMax;
    }

    /**
     * Sets the maximum experience required.
     *
     * @param maxExp the experienceMax to set
     */
    public void setExperienceMax(final Integer maxExp) {
        this.experienceMax = maxExp;
    }

    /**
     * Returns the minimum salary.
     *
     * @return the salaryMin
     */
    public BigDecimal getSalaryMin() {
        return salaryMin;
    }

    /**
     * Sets the minimum salary.
     *
     * @param minSalary the salaryMin to set
     */
    public void setSalaryMin(final BigDecimal minSalary) {
        this.salaryMin = minSalary;
    }

    /**
     * Returns the maximum salary.
     *
     * @return the salaryMax
     */
    public BigDecimal getSalaryMax() {
        return salaryMax;
    }

    /**
     * Sets the maximum salary.
     *
     * @param maxSalary the salaryMax to set
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
     * @return the jobType
     */
    public JobType getJobType() {
        return jobType;
    }

    /**
     * Sets the job type.
     *
     * @param type the jobType to set
     */
    public void setJobType(final JobType type) {
        this.jobType = type;
    }

    /**
     * Returns the current status of the job description.
     *
     * @return the status
     */
    public JDStatus getStatus() {
        return status;
    }

    /**
     * Sets the current status of the job description.
     *
     * @param status the status to set
     */
    public void setStatus(final JDStatus status) {
        this.status = status;
    }

    /**
     * Returns the foreign key ID of the HR user who created this JD.
     *
     * @return the createdById
     */
    public Long getCreatedById() {
        return createdById;
    }

    /**
     * Sets the foreign key ID of the HR user who created this JD.
     *
     * @param userId the createdById to set
     */
    public void setCreatedById(final Long userId) {
        this.createdById = userId;
    }
}
