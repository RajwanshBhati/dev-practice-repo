package com.interview_tracking_system.backend.dto;

import com.interview_tracking_system.backend.enums.JDStatus;
import com.interview_tracking_system.backend.enums.JobType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for Job Description API
 */
public class JDResponseDTO {

    /**
     * Unique identifier for the Job Description
     */
    private UUID id;

    /**
     * Title of the job position
     */
    private String jobTitle;

    /**
     * Detailed description of the job role and responsibilities
     */
    private String jobDescription;

    /**
     * List of skills required for the job
     */
    private List<String> skillsRequired;

    /**
     * Minimum years of experience required
     */
    private Integer minExperience;

    /**
     * Maximum years of experience required
     */
    private Integer maxExperience;

    /**
     * Minimum salary offered
     */
    private BigDecimal minSalary;

    /**
     * Maximum salary offered
     */
    private BigDecimal maxSalary;

    /**
     * Location of the job
     */
    private String location;

    /**
     * Type of the job
     */
    private JobType jobType;

    /**
     * Current status of the Job Description
     */
    private JDStatus status;

    /**
     * Timestamp when the Job Description was created
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp when the Job Description was last updated
     */
    private LocalDateTime updatedAt;

    /**
     * Default constructor
     */
    public JDResponseDTO() {
    }

    /**
     * Parameterized constructor to initialize all fields
     */
    public JDResponseDTO(UUID id, String jobTitle, String jobDescription,
            List<String> skillsRequired,
            Integer minExperience, Integer maxExperience,
            BigDecimal minSalary, BigDecimal maxSalary,
            String location, JobType jobType,
            JDStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
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
     * Returns job description id
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets job description id
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Returns job title
     */
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     * Sets job title
     */
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    /**
     * Returns job description
     */
    public String getJobDescription() {
        return jobDescription;
    }

    /**
     * Sets job description
     */
    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    /**
     * Returns required skills
     */
    public List<String> getSkillsRequired() {
        return skillsRequired == null ? null : new ArrayList<>(skillsRequired);
    }

    /**
     * Sets required skills
     */
    public void setSkillsRequired(List<String> skillsRequired) {
        this.skillsRequired = skillsRequired == null ? null : new ArrayList<>(skillsRequired);
    }

    /**
     * Returns minimum experience
     */
    public Integer getMinExperience() {
        return minExperience;
    }

    /**
     * Sets minimum experience
     */
    public void setMinExperience(Integer minExperience) {
        this.minExperience = minExperience;
    }

    /**
     * Returns maximum experience
     */
    public Integer getMaxExperience() {
        return maxExperience;
    }

    /**
     * Sets maximum experience
     */
    public void setMaxExperience(Integer maxExperience) {
        this.maxExperience = maxExperience;
    }

    /**
     * Returns minimum salary
     */
    public BigDecimal getMinSalary() {
        return minSalary;
    }

    /**
     * Sets minimum salary
     */
    public void setMinSalary(BigDecimal minSalary) {
        this.minSalary = minSalary;
    }

    /**
     * Returns maximum salary
     */
    public BigDecimal getMaxSalary() {
        return maxSalary;
    }

    /**
     * Sets maximum salary
     */
    public void setMaxSalary(BigDecimal maxSalary) {
        this.maxSalary = maxSalary;
    }

    /**
     * Returns location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Returns job type
     */
    public JobType getJobType() {
        return jobType;
    }

    /**
     * Sets job type
     */
    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    /**
     * Returns job status
     */
    public JDStatus getStatus() {
        return status;
    }

    /**
     * Sets job status
     */
    public void setStatus(JDStatus status) {
        this.status = status;
    }

    /**
     * Returns creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets creation timestamp
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Returns last updated timestamp
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets last updated timestamp
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
