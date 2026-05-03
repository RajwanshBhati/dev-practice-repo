package com.interview_tracking_system.backend.dto;

import com.interview_tracking_system.backend.enums.JobType;
import com.interview_tracking_system.backend.constants.ValidationMessages;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for creating or updating Job Description
 */
public class JDRequestDTO {

    /**
     * Job title
     */
    @NotBlank(message = ValidationMessages.JOB_TITLE_REQUIRED)
    private String jobTitle;

    /**
     * Job description
     */
    @NotBlank(message = ValidationMessages.JOB_DESCRIPTION_REQUIRED)
    private String jobDescription;

    /**
     * Required skills for the job
     */
    @NotEmpty(message = ValidationMessages.SKILLS_REQUIRED)
    private List<String> skillsRequired;

    /**
     * Minimum experience required
     */
    @NotNull(message = ValidationMessages.MINEXPERIENCE_REQUIRED)
    @Min(value = 0, message = ValidationMessages.EXPERIENCE_NEGATIVE)
    private Integer minExperience;

    /**
     * Maximum experience required
     */
    @NotNull(message = ValidationMessages.MAXEXPERIENCE_REQUIRED)
    @Min(value = 0, message = ValidationMessages.EXPERIENCE_NEGATIVE)
    private Integer maxExperience;

    /**
     * Minimum salary offered
     */
    @NotNull(message = ValidationMessages.MIN_SALARY_REQUIRED)
    @DecimalMin(value = "0.0", inclusive = false, message = ValidationMessages.SALARY_INVALID)
    private BigDecimal minSalary;

    /**
     * Maximum salary offered
     */
    @NotNull(message = ValidationMessages.MAX_SALARY_REQUIRED)
    @DecimalMin(value = "0.0", inclusive = false, message = ValidationMessages.SALARY_INVALID)
    private BigDecimal maxSalary;

    /**
     * Job location
     */
    @NotBlank(message = ValidationMessages.LOCATION_REQUIRED)
    private String location;

    /**
     * Type of job
     */
    @NotNull(message = ValidationMessages.JOB_TYPE_REQUIRED)
    private JobType jobType;

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
}
