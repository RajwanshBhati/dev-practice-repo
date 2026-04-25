package com.interview_tracking_system.backend.dto;

/**
 * DTO for candidate registration request.
 */
public class CandidateRegisterRequest {

    /** Full name of the candidate. */
    private String fullName;

    /** Email address of the candidate. */
    private String email;

    /** Password chosen by the candidate. */
    private String password;

    /** Confirm password for validation. */
    private String confirmPassword;

    /**
     * Returns the full name.
     *
     * @return the full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the full name.
     *
     * @param name the full name to set
     */
    public void setFullName(final String name) {
        this.fullName = name;
    }

    /**
     * Returns the email.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email.
     *
     * @param userEmail the email to set
     */
    public void setEmail(final String userEmail) {
        this.email = userEmail;
    }

    /**
     * Returns the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param userPassword the password to set
     */
    public void setPassword(final String userPassword) {
        this.password = userPassword;
    }

    /**
     * Returns the confirm password.
     *
     * @return the confirm password
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * Sets the confirm password.
     *
     * @param confirm the confirm password to set
     */
    public void setConfirmPassword(final String confirm) {
        this.confirmPassword = confirm;
    }
}
