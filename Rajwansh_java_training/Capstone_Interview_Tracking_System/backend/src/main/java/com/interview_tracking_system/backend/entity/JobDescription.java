package com.interview_tracking_system.backend.entity;

import com.interviewtracker;
import com.interview_tracking_system.backend.enums.JobType;
import com.interview_tracking_system.backend.enums.JDStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "job_descriptions")
public class JobDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String jobTitle;

    @Column(columnDefinition = "TEXT")
    private String jobDescription;

    @ElementCollection
    private List<String> skillsRequired;
    
    @Column(name = "experience", nullable = false)
    private Integer experience;
    
    @Column(name = "salary", nullable = false, precision = 12, scale = 2)
    private BigDecimal salary;

    @Column(name = "location", nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @Enumerated(EnumType.STRING)
    private JDStatus status = JDStatus.ACTIVE;

    // Here I defined a no-argument constructor for the JobDescription entity, which is required by JPA for entity instantiation. This allows the framework to create instances of JobDescription when retrieving data from the database.
    public JobDescription() {}

    // Getter and setter methods for all the fields in the JobDescription entity. These methods provide access to the properties of the JobDescription and allow for modification of those properties when needed.

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

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
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

    public JDStatus getStatus() {
        return status;
    }
}