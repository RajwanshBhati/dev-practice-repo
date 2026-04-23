package com.interview_tracking_system.backend.dto;

import com.interview_tracking_system.backend.enums.JobType;
import com.interview_tracking_system.backend.constants.ValidationMessages;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for creating/updating Job Description.
 */
public class JDRequestDTO {

    @NotBlank(message = ValidationMessages.JOB_TITLE_REQUIRED)
    private String jobTitle;

    @NotBlank(message = ValidationMessages.JOB_DESCRIPTION_REQUIRED)
    private String jobDescription;

    @NotEmpty(message = ValidationMessages.SKILLS_REQUIRED)
    private List<String> skillsRequired;

    @NotNull(message = ValidationMessages.MINEXPERIENCE_REQUIRED)
    @Min(value = 0, message = ValidationMessages.EXPERIENCE_NEGATIVE)
    private Integer minExperience;

    @NotNull(message = ValidationMessages.MAXEXPERIENCE_REQUIRED)
    @Min(value = 0, message = ValidationMessages.EXPERIENCE_NEGATIVE)
    private Integer maxExperience;

    @NotNull(message = ValidationMessages.SALARY_REQUIRED)
    @DecimalMin(value = "0.0", inclusive = false, message = ValidationMessages.SALARY_INVALID)
    private BigDecimal salary;

    @NotBlank(message = ValidationMessages.LOCATION_REQUIRED)
    private String location;

    @NotNull(message = ValidationMessages.JOB_TYPE_REQUIRED)
    private JobType jobType;

    /**
     * Getters and Setters
     *
     * @return the respective field values
     */

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

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
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
}
