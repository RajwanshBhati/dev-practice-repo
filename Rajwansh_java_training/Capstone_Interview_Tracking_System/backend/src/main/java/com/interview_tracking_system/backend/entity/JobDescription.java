package com.interview_tracking_system.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.interview_tracking_system.backend.enums.JDStatus;
import com.interview_tracking_system.backend.enums.JobType;


@Entity
@Table(name = "job_descriptions")
public class JobDescription {

    /**
     * Unique identifier for the job description.
     * UUID is used for better scalability and uniqueness across systems.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Title of the job (e.g., Java Developer, Data Engineer).
     */
    private String jobTitle;

    /**
     * Detailed job description.
     */
    @Column(columnDefinition = "TEXT")
    private String jobDescription;

    /**
     * List of required skills for the job.
     *
     * Stored in a separate table using @ElementCollection.
     */
    @ElementCollection
    @CollectionTable(name = "jd_skills", joinColumns = @JoinColumn(name = "jd_id"))
    @Column(name = "skill")
    private List<String> skillsRequired;

    /**
     * Minimum experience required (in years).
     */
    @Column(name = "experience_min", nullable = false)
    private Integer experienceMin;

    /**
     * Maximum experience allowed (in years).
     */
    @Column(name = "experience_max", nullable = false)
    private Integer experienceMax;

    /**
     * Minimum salary offered.
     */
    @Column(name = "salary_min", nullable = false, precision = 12, scale = 2)
    private BigDecimal salaryMin;

    /**
     * Maximum salary offered.
     */
    @Column(name = "salary_max", nullable = false, precision = 12, scale = 2)
    private BigDecimal salaryMax;

    /**
     * Job location.
     */
    @Column(nullable = false)
    private String location;

    /**
     * Type of job (Full-Time / Contract / Remote).
     */
    @Enumerated(EnumType.STRING)
    private JobType jobType;

    /**
     * Current status of the JD (ACTIVE / INACTIVE).
     * Default is ACTIVE.
     */
    @Enumerated(EnumType.STRING)
    private JDStatus status = JDStatus.ACTIVE;

    /**
     * HR user who created this JD.
     */
    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    /**
     * Default constructor required by JPA.
     */
    public JobDescription() {}

    // GETTERS & SETTERS 

    public UUID getId() {
        return id;
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

    public Integer getExperienceMin() {
        return experienceMin;
    }

    public void setExperienceMin(Integer experienceMin) {
        this.experienceMin = experienceMin;
    }

    public Integer getExperienceMax() {
        return experienceMax;
    }

    public void setExperienceMax(Integer experienceMax) {
        this.experienceMax = experienceMax;
    }

    public BigDecimal getSalaryMin() {
        return salaryMin;
    }

    public void setSalaryMin(BigDecimal salaryMin) {
        this.salaryMin = salaryMin;
    }

    public BigDecimal getSalaryMax() {
        return salaryMax;
    }

    public void setSalaryMax(BigDecimal salaryMax) {
        this.salaryMax = salaryMax;
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

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
}