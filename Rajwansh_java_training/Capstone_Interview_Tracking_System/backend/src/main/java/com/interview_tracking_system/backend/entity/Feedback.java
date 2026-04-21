package com.interview_tracking_system.backend.entity;

import com.interview_tracking_system.backend.enums.FeedbackStatus;


import jakarta.persistence.*;

@Entity
@Table(name = "feedback")
public class Feedback {
    
     /**
     * Unique identifier for the feedback.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Interview for which feedback is given.
     * Many feedback entries can exist for one interview (multiple panel members).
     */
    @ManyToOne
    @JoinColumn(name = "interview_id")
    private Interview interview;
    
    /**
     * Panel member who provided the feedback.
     */
    @ManyToOne
    @JoinColumn(name = "panel_id")
    private Panel panel;
    
    /**
     * Detailed comments provided by the panel.
     */
    @Column(length = 1000)
    private String comments;
    
    /**
     * Strengths observed in the candidate.
     */
    private String strength;

    /**
     * Weaknesses observed in the candidate.
     */
    private String weakness;

    /**
     * Rating given by the panel (typically between 1 to 5).
     */
    private int rating;
    
     /**
     * Final decision of the panel (SELECTED / REJECTED).
     * Uses @Enumerated(EnumType.STRING) for readable DB storage preferred over ORDINAL.
     */
    @Enumerated(EnumType.STRING)
    private FeedbackStatus status;
    
    /**
     * Default constructor required by JPA.
     */
    public Feedback() {}

    // Here I defined Getters & Setters
    public Long getId() { 
        return id; 
    }

    public Interview getInterview() { 
        return interview; 
    }

    public void setInterview(Interview interview) { 
        this.interview = interview; 
    }

    public Panel getPanel() { 
        return panel; 
    }

    public void setPanel(Panel panel) { 
        this.panel = panel; 
    }

    public String getComments() { 
        return comments; 
    }

    public void setComments(String comments) { 
        this.comments = comments; 
    }

    public String getStrength() { 
        return strength; 
    }

    public void setStrength(String strength) { 
        this.strength = strength; 
    }

    public String getWeakness() { 
        return weakness; 
    }

    public void setWeakness(String weakness) { 
        this.weakness = weakness; 
    }

    public int getRating() { 
        return rating; 
    }

    public void setRating(int rating) { 
        this.rating = rating; 
    }

    public FeedbackStatus getStatus() { 
        return status; 
    }

    public void setStatus(FeedbackStatus status) { 
        this.status = status; 
    }
}