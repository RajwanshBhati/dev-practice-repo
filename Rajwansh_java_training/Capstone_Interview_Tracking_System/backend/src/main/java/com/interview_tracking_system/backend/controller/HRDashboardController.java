package com.interview_tracking_system.backend.controller;

import com.interview_tracking_system.backend.dto.HRCandidateFullDTO;
import com.interview_tracking_system.backend.service.HRDashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for HR dashboard operations.
 * Provides endpoints for HR candidate management view.
 */
@RestController
@RequestMapping("/api/hr")
public class HRDashboardController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HRDashboardController.class);

    private final HRDashboardService hrDashboardService;

    /**
     * Constructor injection.
     */
    public HRDashboardController(HRDashboardService hrDashboardService) {
        this.hrDashboardService = hrDashboardService;
    }

    /**
     * Fetch all candidates for HR dashboard.
     *
     * @return list of candidate details
     */
    @GetMapping("/candidates")
    public ResponseEntity<List<HRCandidateFullDTO>> getCandidates() {

        LOGGER.info("HR dashboard request received for candidate list");

        List<HRCandidateFullDTO> response = hrDashboardService.getAllCandidatesForHR();

        return ResponseEntity.ok(response);
    }
}
