package com.interviewtracker.interview_process_tracking.entity;

import com.interviewtracker.interview_process_tracking.enums.JDStatus;
import com.interviewtracker.interview_process_tracking.enums.JobType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "job_descriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

// Here I have defined the JobDescription entity class, which represents the job description in the database. It includes fields for job title, job description, required skills, experience, salary, location, job type, status, and timestamps for creation and updates. The class is annotated with JPA annotations to map it to a database table.
public class JobDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column(name = "job_description", nullable = false, columnDefinition = "TEXT")
    private String jobDescription;

    //This is an element collection that represents the list of skills required for the job. It is stored in a separate table called "jd_skills" with a foreign key reference to the job description.
    @ElementCollection
    @CollectionTable(name = "jd_skills", joinColumns = @JoinColumn(name = "jd_id"))
    @Column(name = "skill")
    private List<String> skillsRequired;

    @Column(name = "experience", nullable = false)
    private Integer experience;        // in years

    @Column(name = "salary", nullable = false, precision = 12, scale = 2)
    private BigDecimal salary;

    @Column(name = "location", nullable = false)
    private String location;
    
    // The job type is represented as an enum and stored as a string in the database. It indicates whether the job is full-time, contract, or remote.
    @Enumerated(EnumType.STRING)
    @Column(name = "job_type", nullable = false)
    private JobType jobType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    // The status of the job description is represented as an enum and stored as a string in the database. It indicates whether the job description is active, inactive, or closed. By default, it is set to ACTIVE when a new job description is created.
    @Builder.Default
    private JDStatus status = JDStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}