package com.interview_tracking_system.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import com.interview_tracking_system.backend.enums.Stage;

@Entity
@Table(name = "interview")
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @Enumerated(EnumType.STRING)
    @Column(name = "stage", nullable = false, length = 50)
    private Stage stage;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "time", nullable = false)
    private LocalTime time;

    @Column(name = "focus_area", length = 255)
    private String focusArea;

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