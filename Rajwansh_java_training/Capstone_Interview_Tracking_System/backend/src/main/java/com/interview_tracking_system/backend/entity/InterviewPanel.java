package com.interview_tracking_system.backend.entity;


import jakarta.persistence.*;
import com.interview_tracking_system.backend.entity.Interview;
import com.interview_tracking_system.backend.entity.Panel;



/**
 * Entity mapping Interview PanelMember join table.
 * One interview supports max 2 panels unique (interview_id, panel_id).
 */
@Entity
@Table(name = "interview_panel", 
 uniqueConstraints = {
        @UniqueConstraint(columnNames = {"interview_id", "panel_id"})
    },
    indexes = {
        @Index(name = "idx_interview_id", columnList = "interview_id"),
        @Index(name = "idx_panel_id", columnList = "panel_id")
    })
public class InterviewPanel {

     /**
     * Unique identifier for the mapping.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Interview to which panel members are assigned.
     * Many panel mappings can exist for one interview.
     */
    @ManyToOne
    @JoinColumn(name = "interview_id", nullable = false)
    private Interview interview;

    /**
     * Panel member assigned to the interview.
     */
    @ManyToOne
    @JoinColumn(name = "panel_id", nullable = false)
    private Panel panel;

    /**
     * Default constructor required by JPA.
     */
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