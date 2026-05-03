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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of PanelService.
 */
@Service
public final class PanelServiceImpl implements PanelService {

    /**
     * Logger instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PanelServiceImpl.class);

    /**
     * Token expiry hours.
     */
    private static final int TOKEN_EXPIRY_HOURS = 24;

    /**
     * User repository.
     */
    private final UserRepository userRepository;

    /**
     * Email service.
     */
    private final EmailService emailService;

    /**
     * Password encoder.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor.
     *
     * @param userRepository  user repository
     * @param emailService    email service
     * @param passwordEncoder password encoder
     */
    public PanelServiceImpl(final UserRepository userRepository,
            final EmailService emailService,
            final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates panel user and sends activation email.
     *
     * @param request panel create request
     * @return success message
     */
    @Override
    @Transactional
    public String createPanel(final PanelCreateRequest request) {

        LOGGER.info("Creating panel user: {}", request.getEmail());

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        LOGGER.info("Panel created successfully with email: {}", request.getEmail());

        String token = UUID.randomUUID().toString();

        User user = new User();
        LOGGER.info("FULL NAME FROM REQUEST: {}", request.getFullName());
        user.setName(request.getFullName());

        user.setEmail(request.getEmail());
        user.setMobile(request.getMobile());
        user.setOrganisation(request.getOrganization());
        user.setDesignation(request.getDesignation());

        user.setRole(Role.PANEL);
        user.setStatus(UserStatus.PENDING);

        user.setPassword(passwordEncoder.encode("TEMP123"));

        user.setActivationToken(token);
        user.setActivationTokenExpiry(LocalDateTime.now().plusHours(TOKEN_EXPIRY_HOURS));

        userRepository.save(user);

        String activationToken = token;

        emailService.sendPanelActivationEmail(
                request.getEmail(),
                request.getFullName(),
                activationToken);

        LOGGER.info("Panel activation email sent: {}", request.getEmail());

        return MessageConstants.PANEL_CREATED;
    }

    /**
     * Activates panel user.
     *
     * @param request activation request
     * @return success message
     */
    @Override
    @Transactional
    public String activatePanel(final PanelActivationRequest request) {

        LOGGER.info("Activating panel user");

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException(MessageConstants.PASSWORD_MISMATCH);
        }

        User user = userRepository.findByActivationToken(request.getToken())
                .orElseThrow(() -> new RuntimeException(MessageConstants.INVALID_TOKEN));

        if (user.getActivationTokenExpiry() == null
                || user.getActivationTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException(MessageConstants.INVALID_TOKEN);
        }

        user.setPassword(passwordEncoder.encode(request.getConfirmPassword()));
        user.setStatus(UserStatus.ACTIVE);

        user.setActivationToken(null);
        user.setActivationTokenExpiry(null);

        userRepository.save(user);

        LOGGER.info("Panel activated successfully: {}", user.getEmail());

        return MessageConstants.PANEL_ACTIVATED;
    }

    /**
     * Fetches all panel users.
     *
     * @return list of panel users
     */
    @Override
    public List<PanelCreateRequest> getAllPanels() {

        LOGGER.info("Fetching all panel users");

        List<User> panels = userRepository.findByRoleOrderByCreatedAtDesc(Role.PANEL);

        List<PanelCreateRequest> response = new ArrayList<>();

        for (User u : panels) {
            PanelCreateRequest dto = new PanelCreateRequest();
            dto.setId(u.getId());
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
