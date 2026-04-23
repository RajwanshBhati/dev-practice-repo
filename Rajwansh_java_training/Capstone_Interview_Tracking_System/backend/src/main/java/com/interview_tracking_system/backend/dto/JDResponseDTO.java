package com.interview_tracking_system.backend.dto;

import com.interview_tracking_system.backend.enums.JDStatus;
import com.interview_tracking_system.backend.enums.JobType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for Job Description API.
 */
public class JDResponseDTO {

    /**
     * Unique identifier for the Job Description.
     *
     * return @id the UUID of the Job Description
     */
    private UUID id;

    /**
     * Title of the job position.
     */
    private String jobTitle;

    /**
     * Detailed description of the job role and responsibilities.
     */
    private String jobDescription;

    /**
     * List of skills required for the job.
     */
    private List<String> skillsRequired;

    /**
     * Minimum years of experience required for the job.
     */
    private Integer minExperience;

    /**
     * Maximum years of experience required for the job.
     */
    private Integer maxExperience;

    /**
     * Minimum salary offered for the job.
     */
    private BigDecimal minSalary;

    /**
     * Maximum salary offered for the job.
     */
    private BigDecimal maxSalary;

    /**
     * Location of the job.
     */
    private String location;

    /**
     * Type of the job (e.g., FULL_TIME, PART_TIME, CONTRACT).
     */
    private JobType jobType;

    /**
     * Current status of the Job Description (e.g., ACTIVE, INACTIVE).
     */
    private JDStatus status;

    /**
     * Timestamp when the Job Description was created.
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp when the Job Description was last updated.
     */
    private LocalDateTime updatedAt;

    /**
     * Default constructor for serialization/deserialization.
     */
    public JDResponseDTO() {
    }

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
        this.skillsRequired = skillsRequired;
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
     * Getters and Setters
     * 
     * @return the respective field values
     */

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public List<String> getSkillsRequired() {
        return skillsRequired;
    }

    public void setSkillsRequired(List<String> skillsRequired) {
        this.skillsRequired = skillsRequired;
    }

    public Integer getMinExperience() {
        return minExperience;
    }

    public void setMinExperience(Integer minExperience) {
        this.minExperience = minExperience;
    }

    public Integer getMaxExperience() {
        return maxExperience;
    }

    public void setMaxExperience(Integer maxExperience) {
        this.maxExperience = maxExperience;
    }

    public BigDecimal getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(BigDecimal minSalary) {
        this.minSalary = minSalary;
    }

    public BigDecimal getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(BigDecimal maxSalary) {
        this.maxSalary = maxSalary;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public JDStatus getStatus() {
        return status;
    }

    public void setStatus(JDStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
