package com.interview_tracking_system.backend.service.impl;

import com.interview_tracking_system.backend.constants.MessageConstants;
import com.interview_tracking_system.backend.dto.PanelActivationRequest;
import com.interview_tracking_system.backend.dto.PanelCreateRequest;
import com.interview_tracking_system.backend.entity.Panel;
import com.interview_tracking_system.backend.repository.PanelRepository;
import com.interview_tracking_system.backend.service.EmailService;
import com.interview_tracking_system.backend.service.PanelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of PanelService
 */
@Service
public class PanelServiceImpl implements PanelService {

    private static final Logger logger = LoggerFactory.getLogger(PanelServiceImpl.class);

    private final PanelRepository panelRepository;
    private final EmailService emailService;

    public PanelServiceImpl(PanelRepository panelRepository,
            EmailService emailService) {
        this.panelRepository = panelRepository;
        this.emailService = emailService;
    }

    /**
     * Create panel member and send activation email
     */
    @Override
    public String createPanel(PanelCreateRequest request) {

        logger.info("Creating panel member: {}", request.getEmail());

        Panel panel = new Panel();
        panel.setName(request.getFullName());
        panel.setEmail(request.getEmail());
        panel.setMobile(request.getMobile());
        panel.setOrganization(request.getOrganization());
        panel.setDesignation(request.getDesignation());
        panel.setActive(false);

        String token = UUID.randomUUID().toString();

        panel.setActivationToken(token);
        panel.setTokenExpiry(LocalDateTime.now().plusHours(24));

        panelRepository.save(panel);

        String link = "http://localhost:3000/panel/activate?token=" + token;

        emailService.sendPanelActivationEmail(
                request.getEmail(),
                request.getFullName(),
                link);

        logger.info("Panel created successfully: {}", request.getEmail());

        return MessageConstants.PANEL_CREATED;
    }

    /**
     * Activate panel account using token
     */
    @Override
    public String activatePanel(PanelActivationRequest request) {

        logger.info("Activating panel with token");

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException(MessageConstants.PASSWORD_MISMATCH);
        }

        Panel panel = panelRepository.findByActivationToken(request.getToken())
                .orElseThrow(() -> new RuntimeException(MessageConstants.INVALID_TOKEN));

        if (panel.getTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException(MessageConstants.INVALID_TOKEN);
        }

        panel.setPassword(request.getPassword());
        panel.setActive(true);
        panel.setActivationToken(null);

        panelRepository.save(panel);

        logger.info("Panel activated: {}", panel.getEmail());

        return MessageConstants.PANEL_ACTIVATED;
    }

    /**
     * Get all panel members
     */
    @Override
    public List<PanelCreateRequest> getAllPanels() {

        logger.info("Fetching all panel members");

        List<Panel> panels = panelRepository.findAll();

        List<PanelCreateRequest> response = new ArrayList<>();

        for (Panel p : panels) {
            PanelCreateRequest dto = new PanelCreateRequest();
            dto.setFullName(p.getName());
            dto.setEmail(p.getEmail());
            dto.setMobile(p.getMobile());
            dto.setOrganization(p.getOrganization());
            dto.setDesignation(p.getDesignation());

            response.add(dto);
        }

        return response;
    }
}
