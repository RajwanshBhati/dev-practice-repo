package com.interview_tracking_system.backend.service.impl;

import com.interview_tracking_system.backend.constants.MessageConstants;
import com.interview_tracking_system.backend.dto.PanelActivationRequest;
import com.interview_tracking_system.backend.dto.PanelCreateRequest;
import com.interview_tracking_system.backend.entity.User;
import com.interview_tracking_system.backend.enums.Role;
import com.interview_tracking_system.backend.enums.UserStatus;
import com.interview_tracking_system.backend.repository.UserRepository;
import com.interview_tracking_system.backend.service.EmailService;
import com.interview_tracking_system.backend.service.PanelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PanelServiceImpl implements PanelService {

    private static final Logger logger = LoggerFactory.getLogger(PanelServiceImpl.class);

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public PanelServiceImpl(UserRepository userRepository,
            EmailService emailService,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Create panel user and send activation email
     */
    @Override
    @Transactional
    public String createPanel(PanelCreateRequest request) {

        logger.info("Creating panel user: {}", request.getEmail());

        // check duplicate email
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        String token = UUID.randomUUID().toString();

        User user = new User();
        logger.info("FULL NAME FROM REQUEST: {}", request.getFullName());
        user.setName(request.getFullName());

        user.setEmail(request.getEmail());
        user.setMobile(request.getMobile());
        user.setOrganisation(request.getOrganization());
        user.setDesignation(request.getDesignation());

        user.setRole(Role.PANEL);
        user.setStatus(UserStatus.PENDING);

        user.setPassword(passwordEncoder.encode("TEMP123"));

        user.setActivationToken(token);
        user.setActivationTokenExpiry(LocalDateTime.now().plusHours(24));

        userRepository.save(user);

        String link = "http://127.0.0.1:5501/panel/activate?token=" + token;

        emailService.sendPanelActivationEmail(
                request.getEmail(),
                request.getFullName(),
                link);

        logger.info("Panel activation email sent: {}", request.getEmail());

        return MessageConstants.PANEL_CREATED;
    }

    /**
     * Activate panel user
     */
    @Override
    @Transactional
    public String activatePanel(PanelActivationRequest request) {

        logger.info("Activating panel user");

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException(MessageConstants.PASSWORD_MISMATCH);
        }

        User user = userRepository.findByActivationToken(request.getToken())
                .orElseThrow(() -> new RuntimeException(MessageConstants.INVALID_TOKEN));

        if (user.getActivationTokenExpiry() == null ||
                user.getActivationTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException(MessageConstants.INVALID_TOKEN);
        }

        user.setPassword(passwordEncoder.encode(request.getConfirmPassword()));
        user.setStatus(UserStatus.ACTIVE);

        user.setActivationToken(null);
        user.setActivationTokenExpiry(null);

        userRepository.save(user);

        logger.info("Panel activated successfully: {}", user.getEmail());

        return MessageConstants.PANEL_ACTIVATED;
    }

    /**
     * Get all panel users
     */
    @Override
    public List<PanelCreateRequest> getAllPanels() {

        logger.info("Fetching all panel users");

        List<User> panels = userRepository.findByRole(Role.PANEL);

        List<PanelCreateRequest> response = new ArrayList<>();

        for (User u : panels) {
            PanelCreateRequest dto = new PanelCreateRequest();
            dto.setFullName(u.getName());
            dto.setEmail(u.getEmail());
            dto.setMobile(u.getMobile());
            dto.setOrganization(u.getOrganisation());
            dto.setDesignation(u.getDesignation());

            response.add(dto);
        }

        return response;
    }
}
