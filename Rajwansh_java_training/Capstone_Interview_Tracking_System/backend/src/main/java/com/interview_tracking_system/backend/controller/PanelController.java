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

import java.util.List;

/**
 * Controller for Panel module
 */
@RestController
@RequestMapping(ApiEndpoints.PANEL)
public class PanelController {

    private static final Logger logger = LoggerFactory.getLogger(PanelController.class);

    private final PanelService panelService;

    public PanelController(PanelService panelService) {
        this.panelService = panelService;
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
}
