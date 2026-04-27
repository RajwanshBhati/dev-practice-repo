package com.interview_tracking_system.backend.service;

import com.interview_tracking_system.backend.dto.PanelActivationRequest;
import com.interview_tracking_system.backend.dto.PanelCreateRequest;
import java.util.List;

/**
 * Service interface for managing panel member operations.
 * Handles creation, activation, and retrieval of panel members.
 */
public interface PanelService {

    /**
     * Creates a new panel member and sends an activation email.
     * Throws an exception if the email is already registered.
     */
    String createPanel(PanelCreateRequest request);

    /**
     * Activates a panel member's account using a one-time token and sets their
     * password.
     * Validates token expiry and ensures password and confirmPassword match.
     */
    String activatePanel(PanelActivationRequest request);

    /**
     * Retrieves all registered panel members regardless of activation status.
     * Returns an empty list if no panel members exist.
     *
     * @return a {@link List} of all panel members;
     */
    List<PanelCreateRequest> getAllPanels();
}
