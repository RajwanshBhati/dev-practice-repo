package com.interview_tracking_system.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Mapping entity between Interview and Panel.
 * One interview can have multiple panel members.
 */
@Entity
@Table(
    name = "interview_panel",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"interview_id", "panel_id"})
    },
    indexes = {
        @Index(name = "idx_interview_id", columnList = "interview_id"),
        @Index(name = "idx_panel_id", columnList = "panel_id")
    }
)
public class InterviewPanel {

    /** Unique identifier for this mapping. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Interview reference. */
    @ManyToOne
    @JoinColumn(name = "interview_id", nullable = false)
    private Interview interview;

    /** Panel member reference. */
    @ManyToOne
    @JoinColumn(name = "panel_id", nullable = false)
    private Panel panel;

    /**
     * Default constructor for JPA.
     */
    public InterviewPanel() {
    }

    /**
     * Returns the unique identifier.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the associated interview.
     *
     * @return the interview
     */
    public Interview getInterview() {
        return interview;
    }

    /**
     * Sets the associated interview.
     *
     * @param interviewRef the interview to set
     */
    public void setInterview(final Interview interviewRef) {
        this.interview = interviewRef;
    }

    /**
     * Returns the associated panel member.
     *
     * @return the panel member
     */
    public Panel getPanel() {
        return panel;
    }

    /**
     * Sets the associated panel member.
     *
     * @param panelMember the panel member to set
     */
    public void setPanel(final Panel panelMember) {
        this.panel = panelMember;
    }
}