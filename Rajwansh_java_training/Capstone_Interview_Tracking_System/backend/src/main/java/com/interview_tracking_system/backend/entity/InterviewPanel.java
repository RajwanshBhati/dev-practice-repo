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

    /** Interview ID reference. */
    @Column(name = "interview_id", nullable = false)
    private Long interviewId;

    /** Panel member ID reference. */
    @Column(name = "panel_id", nullable = false)
    private Long panelId;

    /**
     * Returns mapping ID.
     *
     * @return mapping ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets mapping ID.
     *
     * @param mappingId mapping ID
     */
    public void setId(final Long mappingId) {
        this.id = mappingId;
    }

    /**
     * Returns interview ID.
     *
     * @return interview ID
     */
    public Long getInterviewId() {
        return interviewId;
    }

    /**
     * Sets interview ID.
     *
     * @param interviewRef interview ID
     */
    public void setInterviewId(final Long interviewRef) {
        this.interviewId = interviewRef;
    }

    /**
     * Returns panel ID.
     *
     * @return panel ID
     */
    public Long getPanelId() {
        return panelId;
    }

    /**
     * Sets panel ID.
     *
     * @param panelRef panel ID
     */
    public void setPanelId(final Long panelRef) {
        this.panelId = panelRef;
    }
}
