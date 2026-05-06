package com.interview_tracking_system.backend.constants;

/**
 * Message constants for Panel module.
 */
public final class MessageConstants {

    /**
     * Private constructor to prevent instantiation.
     */
    private MessageConstants() {
    }

    /**
     * Message for successful panel creation.
     */
    public static final String PANEL_CREATED = "Panel member created successfully";

    /**
     * Message for successful panel activation.
     */
    public static final String PANEL_ACTIVATED = "Panel activated successfully";

    /**
     * Message for invalid or expired token.
     */
    public static final String INVALID_TOKEN = "Invalid or expired token";

    /**
     * Message for password mismatch.
     */
    public static final String PASSWORD_MISMATCH = "Password and Confirm Password do not match";
}
