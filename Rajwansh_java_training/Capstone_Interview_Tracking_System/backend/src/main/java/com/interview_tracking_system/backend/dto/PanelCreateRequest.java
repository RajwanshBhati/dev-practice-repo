package com.interview_tracking_system.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating a panel member.
 */
public class PanelCreateRequest {

    /** Unique ID of panel member. */
    private Long id;

    /** Maximum allowed length for name fields. */
    private static final int MAX_NAME_LENGTH = 80;

    /** Full name of the panel member. */
    @NotBlank(message = "Panel name is required")
    @Size(min = 2, max = MAX_NAME_LENGTH, message = "Panel name must be between 2 and 80 characters")
    private String fullName;

    /** Email address of the panel member. */
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    /** Mobile phone number of the panel member. */
    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Enter a valid 10-digit mobile number")
    private String mobile;

    /** Organization the panel member belongs to. */
    @NotBlank(message = "Organization is required")
    private String organization;

    /** Job designation of the panel member. */
    @NotBlank(message = "Designation is required")
    private String designation;

    /**
     * Returns panel ID.
     *
     * @return panel ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets panel ID.
     *
     * @param panelId panel ID
     */
    public void setId(final Long panelId) {
        this.id = panelId;
    }

    /**
     * Returns full name.
     *
     * @return full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets full name.
     *
     * @param name full name
     */
    public void setFullName(final String name) {
        this.fullName = name;
    }

    /**
     * Returns email.
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param userEmail email
     */
    public void setEmail(final String userEmail) {
        this.email = userEmail;
    }

    /**
     * Returns mobile number.
     *
     * @return mobile number
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Sets mobile number.
     *
     * @param mobileNumber mobile number
     */
    public void setMobile(final String mobileNumber) {
        this.mobile = mobileNumber;
    }

    /**
     * Returns organization.
     *
     * @return organization
     */
    public String getOrganization() {
        return organization;
    }

    /**
     * Sets organization.
     *
     * @param org organization name
     */
    public void setOrganization(final String org) {
        this.organization = org;
    }

    /**
     * Returns designation.
     *
     * @return designation
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * Sets designation.
     *
     * @param roleDesignation designation
     */
    public void setDesignation(final String roleDesignation) {
        this.designation = roleDesignation;
    }
}
