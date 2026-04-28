package com.interview_tracking_system.backend.controller;

import com.interview_tracking_system.backend.constants.ApiEndpoints;
import com.interview_tracking_system.backend.dto.PanelActivationRequest;
import com.interview_tracking_system.backend.dto.PanelCreateRequest;
import com.interview_tracking_system.backend.service.PanelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import com.interview_tracking_system.backend.dto.PanelInterviewDTO;
import com.interview_tracking_system.backend.entity.User;
import com.interview_tracking_system.backend.repository.UserRepository;
import com.interview_tracking_system.backend.service.InterviewService;
import org.springframework.security.core.Authentication;

import java.util.List;

/**
 * Controller for Panel module
 */
@RestController
@RequestMapping(ApiEndpoints.PANEL)
public class PanelController {

    private static final Logger logger = LoggerFactory.getLogger(PanelController.class);

    private final PanelService panelService;
    private final InterviewService interviewService;
    private final UserRepository userRepository;

    public PanelController(PanelService panelService,
            InterviewService interviewService,
            UserRepository userRepository) {
        this.panelService = panelService;
        this.interviewService = interviewService;
        this.userRepository = userRepository;
    }

    /**
     * Create panel member
     */
    @PostMapping(ApiEndpoints.CREATE)
    public String createPanel(@RequestBody PanelCreateRequest request) {
        logger.info("API: Create Panel");
        return panelService.createPanel(request);
    }

    /**
     * Activate panel account
     */
    @PostMapping(ApiEndpoints.ACTIVATE)
    public String activatePanel(@RequestBody PanelActivationRequest request) {
        logger.info("API: Activate Panel");
        return panelService.activatePanel(request);
    }

    /**
     * Get all panel members
     */
    @GetMapping(ApiEndpoints.LIST)
    public List<PanelCreateRequest> getAllPanels() {
        logger.info("API: Get Panels");
        return panelService.getAllPanels();
    }

    /**
     * Fetch panel dashboard interviews for logged-in panel
     */
    @GetMapping(ApiEndpoints.GETPANEL)
    public List<PanelInterviewDTO> getPanelInterviews(final Authentication authentication) {

        String email = authentication.getName();

        logger.info("API: Fetch Panel Interviews for {}", email);

        User panelUser = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("Panel not found for email: {}", email);
                    return new RuntimeException("Panel not found");
                });

        return interviewService.getPanelInterviews(panelUser.getId());
    }
}
