package com.interview_tracking_system.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Mapping entity between Interview and Panel.
 * One interview can have multiple panel members.
 */
@Entity
@Table(name = "interview_panel", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "interview_id", "panel_id" })
}, indexes = {
        @Index(name = "idx_interview_id", columnList = "interview_id"),
        @Index(name = "idx_panel_id", columnList = "panel_id")
})
public class InterviewPanel {

    /** Unique identifier for this mapping. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Foreign key referencing the interview. */
    @Column(name = "interview_id", nullable = false)
    private Long interviewId;

    /** Foreign key referencing the panel member. */
    @Column(name = "panel_id", nullable = false)
    private Long panelId;

    /**
     * Returns the unique identifier.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the foreign key ID of the associated interview.
     *
     * @return the interviewId
     */
    public Long getInterviewId() {
        return interviewId;
    }

    /**
     * Sets the foreign key ID of the associated interview.
     *
     * @param interviewRef the interviewId to set
     */
    public void setInterviewId(final Long interviewRef) {
        this.interviewId = interviewRef;
    }

    /**
     * Returns the foreign key ID of the associated panel member.
     *
     * @return the panelId
     */
    public Long getPanelId() {
        return panelId;
    }

    /**
     * Sets the foreign key ID of the associated panel member.
     *
     * @param panelRef the panelId to set
     */
    public void setPanelId(final Long panelRef) {
        this.panelId = panelRef;
    }
}
