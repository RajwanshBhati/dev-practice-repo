package com.interview_tracking_system.backend.entity;

import jakarta.persistence.*;

@Entity
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;
    
    @Column(unique = true, nullable = false, length = 15, name = "mobile_number")
    private String mobile;
    
    @Column(name = "resume_url", nullable = false)
    private String resumeUrl;
    
    @Column(name = "current_company")
    private String currentCompany;
    
    @Column(name = "total_experience", nullable = false)
    private double totalExp;
    
    @Column(name = "relevant_experience", nullable = false)
    private double relevantExp;
    
    @Column(name = "current_ctc", nullable = false, precision = 12, scale = 2)
    private double currentCtc;

    @Column(name = "expected_ctc", nullable = false, precision = 12, scale = 2)
    private double expectedCtc;
   
    @Column(name = "notice_period", nullable = false, columnDefinition = "INT")
    private int noticePeriod;
    
    @Column(name = "preferred_location", nullable = false)
    private String preferredLocation;
    

    @Enumerated(EnumType.STRING)
    private Stage status;

    @ManyToOne
    @JoinColumn(name = "jd_id")
    private JobDescription jobDescription;

    // Here I defined a no-argument constructor for the Candidate entity, which is required by JPA for entity instantiation. This allows the framework to create instances of Candidate when retrieving data from the database.
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