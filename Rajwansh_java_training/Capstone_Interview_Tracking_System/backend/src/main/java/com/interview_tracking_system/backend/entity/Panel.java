package com.interview_tracking_system.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "panel")
public class Panel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "mobile", length = 15)
    private String mobile;

    @Column(name = "organization", length = 150)
    private String organization;

    @Column(name = "designation", length = 100)
    private String designation;

    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive;

    public Panel() {}

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