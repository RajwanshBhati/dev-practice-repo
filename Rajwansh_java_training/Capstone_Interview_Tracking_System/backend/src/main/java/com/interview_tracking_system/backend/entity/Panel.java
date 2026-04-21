package com.interview_tracking_system.backend.entity;

import jakarta.persistence.*;

/**
 * Maps entity to "panel" table with index "idx_panel_email" on email column for faster queries.
 */
@Entity
@Table(name = "panel", indexes = {
        @Index(name = "idx_panel_email", columnList = "email")
    })
public class Panel {

   /**
     * Unique identifier for the panel member.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    /**
     * Full name of the panel member.
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * Email of the panel member (must be unique).
     */
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    /**
     * Mobile number of the panel member.
     */
    @Column(name = "mobile", length = 15)
    private String mobile;

    /**
     * Organization of the panel member.
     */
    @Column(name = "organization", length = 150)
    private String organization;

    /**
     * Designation of the panel member.
     */
    @Column(name = "designation", length = 100)
    private String designation;

    /**
     * Indicates whether the panel member is active.
     *
     * Default value is TRUE. Inactive panel members cannot be assigned to interviews.
     */
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    /**
     * Default constructor required by JPA.
     */
    public Panel() {

    }

    // Here I defined getter & Setter
    public Long getId() { return id; }

    public String getName() { 
        return name; 
    }

    public void setName(String name) { 
        this.name = name; 
    }

    public String getEmail() { 
        return email; 
    }

    public void setEmail(String email) { 
        this.email = email; 
    }

    public String getMobile() { 
        return mobile; 
    }

    public void setMobile(String mobile) { 
        this.mobile = mobile; 
    }

    public String getOrganization() { 
        return organization; 
    }

    public void setOrganization(String organization) { 
        this.organization = organization; 
    }

    public String getDesignation() { 
        return designation; 
    }

    public void setDesignation(String designation) { 
        this.designation = designation; 
    }

    public boolean isActive() { 
        return isActive; 
    }

    public void setActive(boolean active) { 
        isActive = active; 
    }

}