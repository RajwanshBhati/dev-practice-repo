package com.interview_tracking_system.backend.controller;

import com.interview_tracking_system.backend.constants.ApiEndpoints;
import com.interview_tracking_system.backend.dto.PanelActivationRequest;
import com.interview_tracking_system.backend.dto.PanelCreateRequest;
import com.interview_tracking_system.backend.dto.PanelInterviewDTO;
import com.interview_tracking_system.backend.entity.User;
import com.interview_tracking_system.backend.repository.UserRepository;
import com.interview_tracking_system.backend.service.InterviewService;
import com.interview_tracking_system.backend.service.PanelService;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for Panel module.
 */
@RestController
@RequestMapping(ApiEndpoints.PANEL)
public class PanelController {

    /**
     * Logger instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PanelController.class);

    /**
     * Panel service.
     */
    private final PanelService panelService;

    /**
     * Interview service.
     */
    private final InterviewService interviewService;

    /**
     * User repository.
     */
    private final UserRepository userRepository;

    /**
     * Constructor for PanelController.
     *
     * @param panelService     the panel service
     * @param interviewService the interview service
     * @param userRepository   the user repository
     */
    public PanelController(final PanelService panelService,
            final InterviewService interviewService,
            final UserRepository userRepository) {
        this.panelService = panelService;
        this.interviewService = interviewService;
        this.userRepository = userRepository;
    }

    /**
     * Creates a panel member.
     *
     * @param request the panel create request
     * @return response message
     */
    @PostMapping(ApiEndpoints.CREATE)
    public String createPanel(@Valid @RequestBody final PanelCreateRequest request) {
        LOGGER.info("API: Create Panel");
        return panelService.createPanel(request);
    }

    /**
     * Activates a panel account.
     *
     * @param request the activation request
     * @return response message
     */
    @PostMapping(ApiEndpoints.ACTIVATE)
    public String activatePanel(@Valid @RequestBody final PanelActivationRequest request) {
        LOGGER.info("API: Activate Panel");
        return panelService.activatePanel(request);
    }

    /**
     * Fetches all panel members.
     *
     * @return list of panel members
     */
    @GetMapping(ApiEndpoints.LIST)
    public List<PanelCreateRequest> getAllPanels() {
        LOGGER.info("API: Get Panels");
        return panelService.getAllPanels();
    }

    /**
     * Fetches panel interviews for logged-in user.
     *
     * @param authentication the authentication object
     * @return list of panel interviews
     */
    @GetMapping(ApiEndpoints.GETPANEL)
    public List<PanelInterviewDTO> getPanelInterviews(final Authentication authentication) {

        String email = authentication.getName();

        LOGGER.info("API: Fetch Panel Interviews for {}", email);

        User panelUser = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> {
                    LOGGER.error("Panel not found for email: {}", email);
                    return new RuntimeException("Panel not found");
                });

        return interviewService.getPanelInterviews(panelUser.getId());
    }
}
