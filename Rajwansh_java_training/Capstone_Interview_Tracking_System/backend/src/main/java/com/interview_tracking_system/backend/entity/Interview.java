package com.interview_tracking_system.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import com.interview_tracking_system.backend.enums.Stage;

@Entity
@Table(name = "interview",  indexes = {
        @Index(name = "idx_candidate_id", columnList = "candidate_id")
    })
public class Interview {
    /**
    * Unique identifier for the interview.
    * Uses @GeneratedValue with IDENTITY strategy for auto-incrementing primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
    
    /**
    * Candidate associated with the interview.
    * Many interviews can be associated with one candidate (OneToMany relationship).
    * Uses @ManyToOne to define the relationship and @JoinColumn to specify the foreign key column.
    */
    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;
    
        
    /**
        * Current stage of the interview process .
        * Uses @Enumerated(EnumType.STRING) to store enum values as strings in the database for better readability.
        * The 'stage' field is marked as non-nullable to ensure that every interview has a defined stage.
    */
    @Enumerated(EnumType.STRING)
    @Column(name = "stage", nullable = false, length = 50)
    private Stage stage;
   
    /**
     * Date of the interview.
     */
    @Column(name = "date", nullable = false)
    private LocalDate date;
    
    /**
     * Time of the interview.
    */
    @Column(name = "time", nullable = false)
    private LocalTime time;
    
    /**
     * Focus area for evaluation (e.g., DSA, System Design, Java).
     */
    @Column(name = "focus_area", length = 255)
    private String focusArea;

    /**
     * Default constructor required by JPA.
    */
    public Interview() {}

    // Here I defined getter and setter
    public Long getId() { 
        return id; 
    }

    public Candidate getCandidate() { 
        return candidate; 
    }

    public void setCandidate(Candidate candidate) { 
        this.candidate = candidate; 
    }

    public Stage getStage() { 
        return stage; 
    }

    public void setStage(Stage stage) { 
        this.stage = stage; 
    }

    public LocalDate getDate() { 
        return date; 
    }

    public void setDate(LocalDate date) { 
        this.date = date; 
    }

    public LocalTime getTime() { 
        return time; 
    }

    public void setTime(LocalTime time) { 
        this.time = time; 
    }

    public String getFocusArea() { 
        return focusArea; 
    }

    public void setFocusArea(String focusArea) { 
        this.focusArea = focusArea; 
    }
}