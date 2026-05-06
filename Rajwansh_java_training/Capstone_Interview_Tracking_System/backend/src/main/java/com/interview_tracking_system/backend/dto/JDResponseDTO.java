package com.interview_tracking_system.backend.dto;

import com.interview_tracking_system.backend.enums.JDStatus;
import com.interview_tracking_system.backend.enums.JobType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for Job Description API.
 */
public class JDResponseDTO {

    /** Job description ID. */
    private UUID id;

    /** Job title. */
    private String jobTitle;

    /** Job description text. */
    private String jobDescription;

    /** List of required skills. */
    private List<String> skillsRequired;

    /** Minimum experience required. */
    private Integer minExperience;

    /** Maximum experience required. */
    private Integer maxExperience;

    /** Minimum salary offered. */
    private BigDecimal minSalary;

    /** Maximum salary offered. */
    private BigDecimal maxSalary;

    /** Job location. */
    private String location;

    /** Job type. */
    private JobType jobType;

    /** Job status. */
    private JDStatus status;

    /** Created timestamp. */
    private LocalDateTime createdAt;

    /** Updated timestamp. */
    private LocalDateTime updatedAt;

    /**
     * Default constructor.
     */
    public JDResponseDTO() {
    }

    /**
     * Constructs JDResponseDTO.
     *
     * @param id             job description ID
     * @param jobTitle       job title
     * @param jobDescription job description
     * @param skillsRequired required skills
     * @param minExperience  minimum experience
     * @param maxExperience  maximum experience
     * @param minSalary      minimum salary
     * @param maxSalary      maximum salary
     * @param location       job location
     * @param jobType        job type
     * @param status         job status
     * @param createdAt      created timestamp
     * @param updatedAt      updated timestamp
     */
    @SuppressWarnings("checkstyle:ParameterNumber")
    public JDResponseDTO(
            final UUID id,
            final String jobTitle,
            final String jobDescription,
            final List<String> skillsRequired,
            final Integer minExperience,
            final Integer maxExperience,
            final BigDecimal minSalary,
            final BigDecimal maxSalary,
            final String location,
            final JobType jobType,
            final JDStatus status,
            final LocalDateTime createdAt,
            final LocalDateTime updatedAt) {

        this.id = id;
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.skillsRequired = skillsRequired == null
                ? null
                : new ArrayList<>(skillsRequired);
        this.minExperience = minExperience;
        this.maxExperience = maxExperience;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.location = location;
        this.jobType = jobType;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Returns job description ID.
     *
     * @return ID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets job description ID.
     *
     * @param jdId ID
     */
    public void setId(final UUID jdId) {
        this.id = jdId;
    }

    /**
     * Returns job title.
     *
     * @return job title
     */
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     * Sets job title.
     *
     * @param title job title
     */
    public void setJobTitle(final String title) {
        this.jobTitle = title;
    }

    /**
     * Returns job description.
     *
     * @return job description
     */
    public String getJobDescription() {
        return jobDescription;
    }

    /**
     * Sets job description.
     *
     * @param description job description
     */
    public void setJobDescription(final String description) {
        this.jobDescription = description;
    }

    /**
     * Returns required skills.
     *
     * @return skills list
     */
    public List<String> getSkillsRequired() {
        return skillsRequired == null ? null : new ArrayList<>(skillsRequired);
    }

    /**
     * Sets required skills.
     *
     * @param skills skills list
     */
    public void setSkillsRequired(final List<String> skills) {
        this.skillsRequired = skills == null ? null : new ArrayList<>(skills);
    }

    /**
     * Returns minimum experience.
     *
     * @return minimum experience
     */
    public Integer getMinExperience() {
        return minExperience;
    }

    /**
     * Sets minimum experience.
     *
     * @param minExp minimum experience
     */
    public void setMinExperience(final Integer minExp) {
        this.minExperience = minExp;
    }

    /**
     * Returns maximum experience.
     *
     * @return maximum experience
     */
    public Integer getMaxExperience() {
        return maxExperience;
    }

    /**
     * Sets maximum experience.
     *
     * @param maxExp maximum experience
     */
    public void setMaxExperience(final Integer maxExp) {
        this.maxExperience = maxExp;
    }

    /**
     * Returns minimum salary.
     *
     * @return minimum salary
     */
    public BigDecimal getMinSalary() {
        return minSalary;
    }

    /**
     * Sets minimum salary.
     *
     * @param minSal minimum salary
     */
    public void setMinSalary(final BigDecimal minSal) {
        this.minSalary = minSal;
    }

    /**
     * Returns maximum salary.
     *
     * @return maximum salary
     */
    public BigDecimal getMaxSalary() {
        return maxSalary;
    }

    /**
     * Sets maximum salary.
     *
     * @param maxSal maximum salary
     */
    public void setMaxSalary(final BigDecimal maxSal) {
        this.maxSalary = maxSal;
    }

    /**
     * Returns job location.
     *
     * @return location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets job location.
     *
     * @param jobLocation location
     */
    public void setLocation(final String jobLocation) {
        this.location = jobLocation;
    }

    /**
     * Returns job type.
     *
     * @return job type
     */
    public JobType getJobType() {
        return jobType;
    }

    /**
     * Sets job type.
     *
     * @param type job type
     */
    public void setJobType(final JobType type) {
        this.jobType = type;
    }

    /**
     * Returns job status.
     *
     * @return status
     */
    public JDStatus getStatus() {
        return status;
    }

    /**
     * Sets job status.
     *
     * @param jobStatus status
     */
    public void setStatus(final JDStatus jobStatus) {
        this.status = jobStatus;
    }

    /**
     * Returns created timestamp.
     *
     * @return created timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets created timestamp.
     *
     * @param created created timestamp
     */
    public void setCreatedAt(final LocalDateTime created) {
        this.createdAt = created;
    }

    /**
     * Returns updated timestamp.
     *
     * @return updated timestamp
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets updated timestamp.
     *
     * @param updated updated timestamp
     */
    public void setUpdatedAt(final LocalDateTime updated) {
        this.updatedAt = updated;
    }
}
