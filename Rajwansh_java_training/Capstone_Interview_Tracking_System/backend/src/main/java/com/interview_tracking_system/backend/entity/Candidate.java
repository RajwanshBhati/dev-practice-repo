package com.interview_tracking_system.backend.entity;

import jakarta.persistence.*;
import com.interview_tracking_system.backend.enums.Stage;
import com.interview_tracking_system.backend.enums.Role;

@Entity
@Table(name = "candidates", indexes = {
        @Index(name = "idx_email", columnList = "email"),
        @Index(name = "idx_mobile", columnList = "mobile_number")
    })
public class Candidate {
    
    /**
     * Unique identifier for the candidate.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Full name of the candidate.
     */
    private String name;

    /**
     * Email address of the candidate must be unique.
     */
    @Column(unique = true)
    private String email;
    
    /**
     * Mobile number of the candidate must be unique.
     */
    @Column(unique = true, nullable = false, length = 15, name = "mobile_number")
    private String mobile;
    
     /**
     * URL/path of the uploaded resume file.
     */
    @Column(name = "resume_url", nullable = false)
    private String resumeUrl;
    
    /**
     * Current company of the candidate.
     */
    @Column(name = "current_company")
    private String currentCompany;
    
    /**
     * Total years of experience.
     */
    @Column(name = "total_experience", nullable = false)
    private double totalExp;
    
     /**
     * Relevant experience for the applied role.
     */
    @Column(name = "relevant_experience", nullable = false)
    private double relevantExp;
    
     /**
     * Current CTC of the candidate I used precision and scale to define the format of the CTC values in the database, allowing for up to 12 digits in total with 2 decimal places for cents.
     */
    @Column(name = "current_ctc", nullable = false, precision = 12, scale = 2)
    private double currentCtc;

    /**
     * Expected CTC.
     */
    @Column(name = "expected_ctc", nullable = false, precision = 12, scale = 2)
    private double expectedCtc;
   
     /**
     * Notice period in days.
     */
    @Column(name = "notice_period", nullable = false, columnDefinition = "INT")
    private int noticePeriod;
    
     /**
     * Preferred job location of the candidate.
     */
    @Column(name = "preferred_location", nullable = false)
    private String preferredLocation;
    
    /**
     * Current stage of the candidate in the interview process.
     */
    @Enumerated(EnumType.STRING)
    private Stage status;
    
    /**
     * Job Description (JD) to which the candidate has applied.
     */
    @ManyToOne
    @JoinColumn(name = "jd_id")
    private JobDescription jobDescription;

     /**
     * Default constructor required by JPA.
     * Initializes the candidate status to PROFILING.
     */
    public Candidate() {

    }

    // Getter and setter methods for all the fields in the Candidate entity. These methods provide access to the properties of the Candidate and allow for modification of those properties when needed.
    public Long getId() { 
        return id; 
    }

    public String getName() { 
        return name; 
    }

    public void setName(String name) { 
        this.name = name; 
    }

    public String getEmail() { 
        return email; 
    }

    public void setEmail(String email) { 
        this.email = email; 
    }

    public String getMobile() { 
        return mobile; 
    }

    public void setMobile(String mobile) { 
        this.mobile = mobile; 
    }


    public String getResumeUrl() { 
        return resumeUrl; 
    }

    public void setResumeUrl(String resumeUrl) { 
        this.resumeUrl = resumeUrl; 
    }

    public String getCurrentCompany() { 
        return currentCompany; 
    }

    public void setCurrentCompany(String currentCompany) { 
        this.currentCompany = currentCompany; 
    }

    public double getTotalExp() { 
        return totalExp; 
    }

    public void setTotalExp(double totalExp) { 
        this.totalExp = totalExp; 
    }


    public double getRelevantExp() { 
        return relevantExp;
    }

    public void setRelevantExp(double relevantExp) { 
        this.relevantExp = relevantExp; 
    }

    public double getCurrentCtc() { 
        return currentCtc; 
    }

    public void setCurrentCtc(double currentCtc) { 
        this.currentCtc = currentCtc;
    }

    public double getExpectedCtc() { 
        return expectedCtc; 
    }

    public void setExpectedCtc(double expectedCtc) { 
        this.expectedCtc = expectedCtc; 
    }

    public int getNoticePeriod() { 
        return noticePeriod; 
    }

    public void setNoticePeriod(int noticePeriod) { 
        this.noticePeriod = noticePeriod; 
    }

    public String getPreferredLocation() { 
        return preferredLocation; 
    }

    public void setPreferredLocation(String preferredLocation) { 
        this.preferredLocation = preferredLocation; 
    }

    public Stage getStatus() { 
        return status; 
    }

    public void setStatus(Stage status) { 
        this.status = status; 
    }

    public JobDescription getJobDescription() { 
        return jobDescription; 
    }

    public void setJobDescription(JobDescription jobDescription) { 
        this.jobDescription = jobDescription; 
    }
}