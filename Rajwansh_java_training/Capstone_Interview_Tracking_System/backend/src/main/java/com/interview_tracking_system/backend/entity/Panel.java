package com.interview_tracking_system.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * Represents a panel member who conducts interviews.
 */
@Entity
@Table(
    name = "panel",
    indexes = {
        @Index(name = "idx_panel_email", columnList = "email")
    }
)
public class Panel {

    /** Maximum length for name fields. */
    private static final int NAME_LENGTH = 100;

    /** Maximum length for email field. */
    private static final int EMAIL_LENGTH = 150;

    /** Maximum length for mobile number field. */
    private static final int MOBILE_LENGTH = 15;

    /** Maximum length for organization field. */
    private static final int ORG_LENGTH = 150;

    /** Unique identifier for the panel member. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Full name of the panel member. */
    @Column(nullable = false, length = NAME_LENGTH)
    private String name;

    /** Unique email of the panel member. */
    @Column(nullable = false, unique = true, length = EMAIL_LENGTH)
    private String email;

    /** Mobile number of the panel member. */
    @Column(length = MOBILE_LENGTH)
    private String mobile;

    /** Organization the panel member belongs to. */
    @Column(length = ORG_LENGTH)
    private String organization;

    /** Designation or title of the panel member. */
    @Column(length = NAME_LENGTH)
    private String designation;

    /** Whether this panel member is currently active. */
    @Column(nullable = false)
    private boolean active = true;

    /**
     * Default constructor for JPA.
     */
    public Panel() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String memberName) {
        this.name = memberName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String memberEmail) {
        this.email = memberEmail;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(final String memberMobile) {
        this.mobile = memberMobile;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(final String org) {
        this.organization = org;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(final String memberDesignation) {
        this.designation = memberDesignation;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean activeStatus) {
        this.active = activeStatus;
    }
}