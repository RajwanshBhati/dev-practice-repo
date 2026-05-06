package com.interview_tracking_system.backend.dto;

import com.interview_tracking_system.backend.constants.ValidationMessages;
import com.interview_tracking_system.backend.enums.JobType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for creating or updating job description.
 */
public class JDRequestDTO {

    /** Job title. */
    @NotBlank(message = ValidationMessages.JOB_TITLE_REQUIRED)
    private String jobTitle;

    /** Job description. */
    @NotBlank(message = ValidationMessages.JOB_DESCRIPTION_REQUIRED)
    private String jobDescription;

    /** Required skills for the job. */
    @NotEmpty(message = ValidationMessages.SKILLS_REQUIRED)
    private List<String> skillsRequired;

    /** Minimum experience required. */
    @NotNull(message = ValidationMessages.MINEXPERIENCE_REQUIRED)
    @Min(value = 0, message = ValidationMessages.EXPERIENCE_NEGATIVE)
    private Integer minExperience;

    /** Maximum experience required. */
    @NotNull(message = ValidationMessages.MAXEXPERIENCE_REQUIRED)
    @Min(value = 0, message = ValidationMessages.EXPERIENCE_NEGATIVE)
    private Integer maxExperience;

    /** Minimum salary offered. */
    @NotNull(message = ValidationMessages.MIN_SALARY_REQUIRED)
    @DecimalMin(value = "0.0", inclusive = false, message = ValidationMessages.SALARY_INVALID)
    private BigDecimal minSalary;

    /** Maximum salary offered. */
    @NotNull(message = ValidationMessages.MAX_SALARY_REQUIRED)
    @DecimalMin(value = "0.0", inclusive = false, message = ValidationMessages.SALARY_INVALID)
    private BigDecimal maxSalary;

    /** Job location. */
    @NotBlank(message = ValidationMessages.LOCATION_REQUIRED)
    private String location;

    /** Type of job. */
    @NotNull(message = ValidationMessages.JOB_TYPE_REQUIRED)
    private JobType jobType;

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
     * @return required skills
     */
    public List<String> getSkillsRequired() {
        return skillsRequired == null ? null : new ArrayList<>(skillsRequired);
    }

    /**
     * Sets required skills.
     *
     * @param skills skills required
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
     * @param minimumExperience minimum experience
     */
    public void setMinExperience(final Integer minimumExperience) {
        this.minExperience = minimumExperience;
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
     * @param maximumExperience maximum experience
     */
    public void setMaxExperience(final Integer maximumExperience) {
        this.maxExperience = maximumExperience;
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
     * @param minimumSalary minimum salary
     */
    public void setMinSalary(final BigDecimal minimumSalary) {
        this.minSalary = minimumSalary;
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
     * @param maximumSalary maximum salary
     */
    public void setMaxSalary(final BigDecimal maximumSalary) {
        this.maxSalary = maximumSalary;
    }

    /**
     * Returns location.
     *
     * @return location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets location.
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
}
