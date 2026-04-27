package com.interview_tracking_system.backend.dto;

public class PanelCreateRequest {

    /**
     * Unique id of panel member.
     */
    private Long id;

    /**
     * Full name of the panel member being registered. Must not be null or blank.
     */
    private String fullName;

    /**
     * Email address of the panel member. Must be unique across all panel members.
     */
    private String email;

    /** Mobile phone number of the panel member. Optional. */
    private String mobile;

    /** Organization the panel member belongs to. Optional. */
    private String organization;

    /** Job designation or title of the panel member. Optional. */
    private String designation;

    /**
     * Returns panel id.
     *
     * @return panel id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets panel id.
     *
     * @param id panel id
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Returns the full name of the panel member.
     *
     * @return the full name
     */
    public String getFullName() {
        return fullName;
    }

    public String getOrganization() {
        return organization;
    }

    /**
     * Sets the full name of the panel member.
     *
     * @param fullName the full name to set; must not be null or blank
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Returns the email address of the panel member.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the panel member.
     *
     * @param email the email to set; must be unique and non-null
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * Returns the mobile phone number of the panel member.
     *
     * @return the mobile number
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Sets the mobile phone number of the panel member.
     *
     * @param mobile the mobile number to set; optional
     */
    public void setMobile(final String mobile) {
        this.mobile = mobile;
    }

    /**
     * Returns the organization the panel member belongs to.
     *
     * @return the organization name
     *         public String getOrganization() {
     *         return organization;
     *         }
     *
     *         /**
     *         Sets the organization the panel member belongs to.
     *
     * @param organization the organization name to set; optional
     */
    public void setOrganization(final String organization) {
        this.organization = organization;
    }

    /**
     * Returns the job designation or title of the panel member.
     *
     * @return the designation
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * Sets the job designation or title of the panel member.
     *
     * @param designation the designation to set; optional
     */
    public void setDesignation(final String designation) {
        this.designation = designation;
    }
}
