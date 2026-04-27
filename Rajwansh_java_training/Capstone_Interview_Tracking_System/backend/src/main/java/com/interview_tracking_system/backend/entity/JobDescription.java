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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    @Column(name = "status", nullable = false)
    private JDStatus status = JDStatus.ACTIVE;

    /**
     * Auto-set on record creation.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Auto-updated on every save.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Default constructor.
     */
    public JobDescription() {

    }

    /**
     * All-args constructor.
     */
    public JobDescription(UUID id, String jobTitle, String jobDescription,
            List<String> skillsRequired, Integer minExperience,
            Integer maxExperience, BigDecimal minSalary, BigDecimal maxSalary,
            String location, JobType jobType, JDStatus status,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.skillsRequired = skillsRequired;
        this.experienceMin = minExperience;
        this.experienceMax = maxExperience;
        this.salaryMin = minSalary;
        this.salaryMax = maxSalary;
        this.location = location;
        this.jobType = jobType;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
     * Returns the creation timestamp.
     *
     * @return the createdAt
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns the last updated timestamp.
     *
     * @return the updatedAt
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Returns a new builder instance.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        /** The ID of the job description. */
        private UUID id;
        /** The job title. */
        private String jobTitle;
        /** The detailed job description. */
        private String jobDescription;
        /** The list of required skills. */
        private List<String> skillsRequired;
        /** The minimum experience required in years. */
        private Integer minExperience;
        /** The maximum experience required in years. */
        private Integer maxExperience;
        /** The minimum salary offered. */
        private BigDecimal minSalary;
        /** The maximum salary offered. */
        private BigDecimal maxSalary;
        /** The job location. */
        private String location;
        /** The job type. */
        private JobType jobType;
        /** The status of the job description. */
        private JDStatus status = JDStatus.ACTIVE;

        /**
         * Sets the ID for the job description.
         * 
         * @param id the ID to set
         * @return this builder instance
         */
        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        /**
         * Sets the job title.
         * 
         * @param jobTitle the job title to set
         * @return this builder instance
         */
        public Builder jobTitle(String jobTitle) {
            this.jobTitle = jobTitle;
            return this;
        }

        /**
         * Sets the job description text.
         * 
         * @param jobDescription the job description to set
         * @return this builder instance
         */
        public Builder jobDescription(String jobDescription) {
            this.jobDescription = jobDescription;
            return this;
        }

        /**
         * Sets the list of required skills.
         * 
         * @param skillsRequired the skills required to set
         * @return this builder instance
         */
        public Builder skillsRequired(List<String> skillsRequired) {
            this.skillsRequired = skillsRequired;
            return this;
        }

        /**
         * Sets the minimum experience required.
         * 
         * @param minExperience the minimum experience to set
         * @return this builder instance
         */
        public Builder minExperience(Integer minExperience) {
            this.minExperience = minExperience;
            return this;
        }

        /**
         * Sets the maximum experience required.
         * 
         * @param maxExperience the maximum experience to set
         * @return this builder instance
         */
        public Builder maxExperience(Integer maxExperience) {
            this.maxExperience = maxExperience;
            return this;
        }

        /**
         * Sets the minimum salary offered.
         * 
         * @param minSalary the minimum salary to set
         * @return this builder instance
         */
        public Builder minSalary(BigDecimal minSalary) {
            this.minSalary = minSalary;
            return this;
        }

        /**
         * Sets the maximum salary offered.
         * 
         * @param maxSalary the maximum salary to set
         * @return this builder instance
         */
        public Builder maxSalary(BigDecimal maxSalary) {
            this.maxSalary = maxSalary;
            return this;
        }

        /**
         * Sets the job location.
         * 
         * @param location the location to set
         * @return this builder instance
         */
        public Builder location(String location) {
            this.location = location;
            return this;
        }

        /**
         * Sets the job type.
         * 
         * @param jobType the job type to set
         * @return this builder instance
         */
        public Builder jobType(JobType jobType) {
            this.jobType = jobType;
            return this;
        }

        /**
         * Sets the status of the job description.
         * 
         * @param status the status to set
         * @return this builder instance
         */
        public Builder status(JDStatus status) {
            this.status = status;
            return this;
        }

        /**
         * Builds and returns a JobDescription instance based on the provided values.
         * 
         * @return a new JobDescription instance
         */
        public JobDescription build() {
            JobDescription jd = new JobDescription();
            jd.id = this.id;
            jd.jobTitle = this.jobTitle;
            jd.jobDescription = this.jobDescription;
            jd.skillsRequired = this.skillsRequired;
            jd.experienceMin = this.minExperience;
            jd.experienceMax = this.maxExperience;
            jd.salaryMin = this.minSalary;
            jd.salaryMax = this.maxSalary;
            jd.location = this.location;
            jd.jobType = this.jobType;
            jd.status = this.status;
            return jd;
        }
    }

}
