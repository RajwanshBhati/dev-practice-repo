package com.interview_tracking_system.backend.dto;

/**
 * Request DTO for panel activation.
 * Contains activation token and password details.
 */
public class PanelActivationRequest {

    /**
     * One-time activation token sent to the panel member's registered email.
     */
    private String token;

    /**
     * Desired password chosen by the panel member.
     */
    private String password;

    /**
     * Confirmation of the desired password.
     */
    private String confirmPassword;

    /**
     * Returns the activation token.
     *
     * @return activation token
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the activation token.
     *
     * @param activationToken token value
     */
    public void setToken(final String activationToken) {
        this.token = activationToken;
    }

    /**
     * Returns the password.
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param newPassword password value
     */
    public void setPassword(final String newPassword) {
        this.password = newPassword;
    }

    /**
     * Returns the confirm password.
     *
     * @return confirm password
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * Sets the confirm password.
     *
     * @param confirmPwd confirm password value
     */
    public void setConfirmPassword(final String confirmPwd) {
        this.confirmPassword = confirmPwd;
    }
}
