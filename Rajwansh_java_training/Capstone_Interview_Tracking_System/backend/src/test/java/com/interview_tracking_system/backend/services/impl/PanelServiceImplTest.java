package com.interview_tracking_system.backend.services.impl;

/**
 * Static imports for assertions and Mockito methods.
 */
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Constant import used for response message verification.
 */
import com.interview_tracking_system.backend.constants.MessageConstants;

/**
 * DTO imports used for panel service testing.
 */
import com.interview_tracking_system.backend.dto.PanelActivationRequest;
import com.interview_tracking_system.backend.dto.PanelCreateRequest;

/**
 * Entity import used for panel user test data.
 */
import com.interview_tracking_system.backend.entity.User;

/**
 * Enum imports used for panel role and status.
 */
import com.interview_tracking_system.backend.enums.Role;
import com.interview_tracking_system.backend.enums.UserStatus;

/**
 * Repository import used for mocking user data operations.
 */
import com.interview_tracking_system.backend.repository.UserRepository;

/**
 * Service import used for email sending verification.
 */
import com.interview_tracking_system.backend.service.EmailService;
import com.interview_tracking_system.backend.service.impl.PanelServiceImpl;

/**
 * Java imports used for date time, list and optional values.
 */
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * JUnit imports used for testing.
 */
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Mockito imports used for mocking dependencies.
 */
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Spring security import used for password encoding.
 */
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This class tests PanelServiceImpl.
 *
 * It verifies panel creation, panel activation,
 * panel listing and validation failure scenarios.
 */
@ExtendWith(MockitoExtension.class)
class PanelServiceImplTest {

    /**
     * Mocked user repository.
     */
    @Mock
    private UserRepository userRepository;

    /**
     * Mocked email service.
     */
    @Mock
    private EmailService emailService;

    /**
     * Mocked password encoder.
     */
    @Mock
    private PasswordEncoder passwordEncoder;

    /**
     * Service under test.
     */
    private PanelServiceImpl service;

    /**
     * Initializes service before each test.
     */
    @BeforeEach
    void setUp() {
        service = new PanelServiceImpl(userRepository, emailService, passwordEncoder);
    }

    /**
     * Tests panel creation, activation and panel list retrieval.
     */
    @Test
    void createActivateAndListPanelsShouldWork() {
        PanelCreateRequest request = panelRequest();

        when(userRepository.findByEmail("panel@test.com"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode(anyString()))
                .thenReturn("encoded");

        assertEquals(
                MessageConstants.PANEL_CREATED,
                service.createPanel(request));

        verify(emailService).sendPanelActivationEmail(
                eq("panel@test.com"),
                eq("Panel User"),
                anyString());

        User panel = new User();
        panel.setId(5L);
        panel.setName("Panel User");
        panel.setEmail("panel@test.com");
        panel.setMobile("9876543210");
        panel.setOrganisation("Org");
        panel.setDesignation("Tech Lead");
        panel.setRole(Role.PANEL);
        panel.setStatus(UserStatus.PENDING);
        panel.setActivationTokenExpiry(LocalDateTime.now().plusHours(1));

        when(userRepository.findByActivationToken("token"))
                .thenReturn(Optional.of(panel));

        PanelActivationRequest activate = new PanelActivationRequest();
        activate.setToken("token");
        activate.setPassword("Password@1");
        activate.setConfirmPassword("Password@1");

        assertEquals(
                MessageConstants.PANEL_ACTIVATED,
                service.activatePanel(activate));

        assertEquals(
                UserStatus.ACTIVE,
                panel.getStatus());

        when(userRepository.findByRoleOrderByCreatedAtDesc(Role.PANEL))
                .thenReturn(List.of(panel));

        List<PanelCreateRequest> panels = service.getAllPanels();

        assertEquals(1, panels.size());
        assertEquals("Panel User", panels.get(0).getFullName());
    }

    /**
     * Tests duplicate panel, password mismatch and invalid token cases.
     */
    @Test
    void negativePanelBranchesShouldThrow() {
        when(userRepository.findByEmail("panel@test.com"))
                .thenReturn(Optional.of(new User()));

        assertThrows(
                RuntimeException.class,
                () -> service.createPanel(panelRequest()));

        PanelActivationRequest mismatch = new PanelActivationRequest();
        mismatch.setToken("token");
        mismatch.setPassword("one");
        mismatch.setConfirmPassword("two");

        assertThrows(
                RuntimeException.class,
                () -> service.activatePanel(mismatch));

        PanelActivationRequest invalid = new PanelActivationRequest();
        invalid.setToken("bad");
        invalid.setPassword("same");
        invalid.setConfirmPassword("same");

        when(userRepository.findByActivationToken("bad"))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> service.activatePanel(invalid));
    }

    /**
     * Creates test panel request data.
     *
     * @return test panel create request
     */
    private PanelCreateRequest panelRequest() {
        PanelCreateRequest request = new PanelCreateRequest();
        request.setFullName("Panel User");
        request.setEmail("panel@test.com");
        request.setMobile("9876543210");
        request.setOrganization("Org");
        request.setDesignation("Tech Lead");
        return request;
    }
}
