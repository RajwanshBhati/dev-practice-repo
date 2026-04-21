package com.interview_tracking_system.backend.entity;

import com.interview_tracking_system.backend.enums.FeedbackStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Represents feedback submitted by a panel member for an interview.
 */
@Entity
@Table(name = "feedback")
public final class Feedback {

    /** Maximum length for comments field. */
    private static final int COMMENTS_LENGTH = 1000;

    /**
     * Unique identifier for the feedback.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Interview for which this feedback was given. */
    @ManyToOne
    @JoinColumn(name = "interview_id")
    private Interview interview;

    /** Panel member who submitted this feedback. */
    @ManyToOne
    @JoinColumn(name = "panel_id")
    private Panel panel;

    /** General comments about the candidate. */
    @Column(length = COMMENTS_LENGTH)
    private String comments;

    /** Strengths observed in the candidate. */
    private String strength;

    /** Weaknesses observed in the candidate. */
    private String weakness;

    /** Numeric rating given by the panel member. */
    private int rating;

    /** Current status of the feedback. */
    @Enumerated(EnumType.STRING)
    private FeedbackStatus status;

    /**
     * Default constructor required by JPA.
     */
    public Feedback() {
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
     * Returns the panel member who gave this feedback.
     *
     * @return the panel member
     */
    public Panel getPanel() {
        return panel;
    }

    /**
     * Sets the panel member who gave this feedback.
     *
     * @param panelMember the panel member to set
     */
    public void setPanel(final Panel panelMember) {
        this.panel = panelMember;
    }

    /**
     * Returns the comments.
     *
     * @return the comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets the comments.
     *
     * @param feedbackComments the comments to set
     */
    public void setComments(final String feedbackComments) {
        this.comments = feedbackComments;
    }

    /**
     * Returns the candidate's strengths.
     *
     * @return the strength
     */
    public String getStrength() {
        return strength;
    }

    /**
     * Sets the candidate's strengths.
     *
     * @param candidateStrength the strength to set
     */
    public void setStrength(final String candidateStrength) {
        this.strength = candidateStrength;
    }

    /**
     * Returns the candidate's weaknesses.
     *
     * @return the weakness
     */
    public String getWeakness() {
        return weakness;
    }

    /**
     * Sets the candidate's weaknesses.
     *
     * @param candidateWeakness the weakness to set
     */
    public void setWeakness(final String candidateWeakness) {
        this.weakness = candidateWeakness;
    }

    /**
     * Returns the numeric rating.
     *
     * @return the rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * Sets the numeric rating.
     *
     * @param feedbackRating the rating to set
     */
    public void setRating(final int feedbackRating) {
        this.rating = feedbackRating;
    }

    /**
     * Returns the feedback status.
     *
     * @return the status
     */
    public FeedbackStatus getStatus() {
        return status;
    }

    /**
     * Sets the feedback status.
     *
     * @param feedbackStatus the status to set
     */
    public void setStatus(final FeedbackStatus feedbackStatus) {
        this.status = feedbackStatus;
    }
}