package com.interview_tracking_system.backend.entity;

import com.interview_tracking_system.backend.entity.Interview;
import com.interview_tracking_system.backend.entity.Panel;
import com.interview_tracking_system.backend.enums.FeedbackStatus;


import jakarta.persistence.*;

@Entity
@Table(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "interview_id")
    private Interview interview;

    @ManyToOne
    @JoinColumn(name = "panel_id")
    private Panel panel;

    @Column(length = 1000)
    private String comments;

    private String strength;
    private String weakness;

    private int rating;

    @Enumerated(EnumType.STRING)
    private FeedbackStatus status;

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