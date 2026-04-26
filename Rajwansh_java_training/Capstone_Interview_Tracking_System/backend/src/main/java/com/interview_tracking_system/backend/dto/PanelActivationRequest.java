package com.interview_tracking_system.backend.dto;

public class PanelActivationRequest {

    /**
     * One-time activation token sent to the panel member's registered email.
     * Used to verify the identity of the account being activated.
     */
    private String token;

    /**
     * Desired password chosen by the panel member during activation.
     */
    private String password;

    /**
     * Confirmation of the desired password.
     * Must exactly match
     */
    private String confirmPassword;

    /**
     * Returns the one-time activation token.
     *
     * @return the activation token
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the one-time activation token.
     *
     * @param token the activation token to set; must not be null or blank
     */
    public void setToken(final String token) {
        this.token = token;
    }

    /**
     * Returns the plain-text password provided by the panel member.
     *
     * @return the plain-text password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the plain-text password chosen by the panel member.
     *
     * @param password the password to set; must not be null or blank,
     *                 and should satisfy the application's password policy
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * Returns the confirmation password provided by the panel member.
     *
     * @return the confirmation password
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * Sets the confirmation password.
     *
     * @param confirmPassword the confirmation password to set;
     */
    public void setConfirmPassword(final String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
