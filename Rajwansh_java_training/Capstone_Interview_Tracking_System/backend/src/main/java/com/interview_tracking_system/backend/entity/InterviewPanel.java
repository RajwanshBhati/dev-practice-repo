package com.interview_tracking_system.backend.entity;


import jakarta.persistence.*;
import com.interview_tracking_system.backend.entity.Interview;
import com.interview_tracking_system.backend.entity.Panel;




@Entity
@Table(name = "interview_panel")
public class InterviewPanel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "interview_id")
    private Interview interview;

    @ManyToOne
    @JoinColumn(name = "panel_id")
    private Panel panel;

    public InterviewPanel() {}

    // I defined Getters & Setters here 
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
}