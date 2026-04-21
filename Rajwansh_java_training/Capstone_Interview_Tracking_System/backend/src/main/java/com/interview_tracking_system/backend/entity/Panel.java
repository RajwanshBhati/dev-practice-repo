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
    private boolean isActive = true;

    /**
     * Default constructor for JPA.
     */
    public Panel() {
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
     * Returns the panel member's name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the panel member's name.
     *
     * @param memberName the name to set
     */
    public void setName(final String memberName) {
        this.name = memberName;
    }

    /**
     * Returns the panel member's email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the panel member's email.
     *
     * @param memberEmail the email to set
     */
    public void setEmail(final String memberEmail) {
        this.email = memberEmail;
    }

    /**
     * Returns the panel member's mobile number.
     *
     * @return the mobile number
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Sets the panel member's mobile number.
     *
     * @param memberMobile the mobile number to set
     */
    public void setMobile(final String memberMobile) {
        this.mobile = memberMobile;
    }

    /**
     * Returns the organization name.
     *
     * @return the organization
     */
    public String getOrganization() {
        return organization;
    }

    /**
     * Sets the organization name.
     *
     * @param org the organization to set
     */
    public void setOrganization(final String org) {
        this.organization = org;
    }

    /**
     * Returns the designation.
     *
     * @return the designation
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * Sets the designation.
     *
     * @param memberDesignation the designation to set
     */
    public void setDesignation(final String memberDesignation) {
        this.designation = memberDesignation;
    }

    /**
     * Returns whether the panel member is active.
     *
     * @return true if active
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets the active status of the panel member.
     *
     * @param activeStatus the active status to set
     */
    public void setActive(final boolean activeStatus) {
        this.isActive = activeStatus;
    }
}