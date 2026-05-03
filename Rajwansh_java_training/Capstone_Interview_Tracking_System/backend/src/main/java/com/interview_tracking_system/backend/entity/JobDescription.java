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
     *
     * @param jdId             the unique identifier
     * @param title            the job title
     * @param description      the job description text
     * @param skills           the list of required skills
     * @param minExperience    the minimum experience required
     * @param maxExperience    the maximum experience required
     * @param minSalary        the minimum salary offered
     * @param maxSalary        the maximum salary offered
     * @param jobLocation      the job location
     * @param type             the job type
     * @param jdStatus         the current status
     * @param createdTimestamp the creation timestamp
     * @param updatedTimestamp the last updated timestamp
     */
    @SuppressWarnings("checkstyle:ParameterNumber")
    public JobDescription(final UUID jdId, final String title, final String description,
            final List<String> skills, final Integer minExperience,
            final Integer maxExperience, final BigDecimal minSalary, final BigDecimal maxSalary,
            final String jobLocation, final JobType type, final JDStatus jdStatus,
            final LocalDateTime createdTimestamp, final LocalDateTime updatedTimestamp) {
        this.id = jdId;
        this.jobTitle = title;
        this.jobDescription = description;
        this.skillsRequired = skills == null
                ? null
                : new ArrayList<>(skills);
        this.experienceMin = minExperience;
        this.experienceMax = maxExperience;
        this.salaryMin = minSalary;
        this.salaryMax = maxSalary;
        this.location = jobLocation;
        this.jobType = type;
        this.status = jdStatus;
        this.createdAt = createdTimestamp;
        this.updatedAt = updatedTimestamp;
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
     * Sets the unique identifier.
     *
     * @param id the id to set
     */
    public void setId(final UUID id) {
        this.id = id;
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
     * @param jdStatus the status to set
     */
    public void setStatus(final JDStatus jdStatus) {
        this.status = jdStatus;
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
     * Sets the creation timestamp.
     *
     * @param createdTimestamp the createdAt to set
     */
    public void setCreatedAt(final LocalDateTime createdTimestamp) {
        this.createdAt = createdTimestamp;
    }

    /**
     * Sets the updated timestamp.
     *
     * @param updatedTimestamp the updatedAt to set
     */
    public void setUpdatedAt(final LocalDateTime updatedTimestamp) {
        this.updatedAt = updatedTimestamp;
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
         * @param jdId the ID to set
         * @return this builder instance
         */
        public Builder id(final UUID jdId) {
            this.id = jdId;
            return this;
        }

        /**
         * Sets the job title.
         *
         * @param title the job title to set
         * @return this builder instance
         */
        public Builder jobTitle(final String title) {
            this.jobTitle = title;
            return this;
        }

        /**
         * Sets the job description text.
         *
         * @param description the job description to set
         * @return this builder instance
         */
        public Builder jobDescription(final String description) {
            this.jobDescription = description;
            return this;
        }

        /**
         * Sets the list of required skills.
         *
         * @param skills the skills required to set
         * @return this builder instance
         */
        public Builder skillsRequired(final List<String> skills) {
            this.skillsRequired = skills == null
                    ? null
                    : new ArrayList<>(skills);
            return this;
        }

        /**
         * Sets the minimum experience required.
         *
         * @param minExp the minimum experience to set
         * @return this builder instance
         */
        public Builder minExperience(final Integer minExp) {
            this.minExperience = minExp;
            return this;
        }

        /**
         * Sets the maximum experience required.
         *
         * @param maxExp the maximum experience to set
         * @return this builder instance
         */
        public Builder maxExperience(final Integer maxExp) {
            this.maxExperience = maxExp;
            return this;
        }

        /**
         * Sets the minimum salary offered.
         *
         * @param minSal the minimum salary to set
         * @return this builder instance
         */
        public Builder minSalary(final BigDecimal minSal) {
            this.minSalary = minSal;
            return this;
        }

        /**
         * Sets the maximum salary offered.
         *
         * @param maxSal the maximum salary to set
         * @return this builder instance
         */
        public Builder maxSalary(final BigDecimal maxSal) {
            this.maxSalary = maxSal;
            return this;
        }

        /**
         * Sets the job location.
         *
         * @param jobLocation the location to set
         * @return this builder instance
         */
        public Builder location(final String jobLocation) {
            this.location = jobLocation;
            return this;
        }

        /**
         * Sets the job type.
         *
         * @param type the job type to set
         * @return this builder instance
         */
        public Builder jobType(final JobType type) {
            this.jobType = type;
            return this;
        }

        /**
         * Sets the status of the job description.
         *
         * @param jdStatus the status to set
         * @return this builder instance
         */
        public Builder status(final JDStatus jdStatus) {
            this.status = jdStatus;
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
