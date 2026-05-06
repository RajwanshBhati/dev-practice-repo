package com.interview_tracking_system.backend.controller;

/**
 * Required static imports for assertions and mocking.
 */
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;

/**
 * DTO imports used for request and response testing.
 */
import com.interview_tracking_system.backend.dto.CandidateOnboardRequest;
import com.interview_tracking_system.backend.dto.CandidateProfileRequest;
import com.interview_tracking_system.backend.dto.CandidateRegisterRequest;
import com.interview_tracking_system.backend.dto.CandidateResponseDTO;
import com.interview_tracking_system.backend.dto.LoginRequestDTO;
import com.interview_tracking_system.backend.dto.PanelActivationRequest;
import com.interview_tracking_system.backend.dto.PanelCreateRequest;
import com.interview_tracking_system.backend.dto.PanelInterviewDTO;

/**
 * Entity and enum imports.
 */
import com.interview_tracking_system.backend.entity.User;
import com.interview_tracking_system.backend.enums.Role;
import com.interview_tracking_system.backend.enums.Stage;

/**
 * Repository and utility imports.
 */
import com.interview_tracking_system.backend.repository.UserRepository;
import com.interview_tracking_system.backend.security.JwtUtil;

/**
 * Service layer imports.
 */
import com.interview_tracking_system.backend.service.CandidateService;
import com.interview_tracking_system.backend.service.InterviewService;
import com.interview_tracking_system.backend.service.PanelService;

/**
 * Java utility imports.
 */
import java.util.List;
import java.util.Optional;

/**
 * Testing framework imports.
 */
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Mockito related imports.
 */
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Spring testing and security imports.
 */
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * This class tests CandidateController and PanelController.
 *
 * It verifies that all controller methods correctly call the service layer
 * and return expected responses.
 */
@ExtendWith(MockitoExtension.class)
class CandidateAndPanelControllerTest {

        /**
         * Mocked dependencies for controller testing.
         */
        @Mock
        private CandidateService candidateService;
        @Mock
        private JwtUtil jwtUtil;
        @Mock
        private PanelService panelService;
        @Mock
        private InterviewService interviewService;
        @Mock
        private UserRepository userRepository;

        /**
         * Controller instances under test.
         */
        private CandidateController candidateController;
        private PanelController panelController;

        /**
         * Initializes controllers before each test execution.
         */
        @BeforeEach
        void setUp() {
                candidateController = new CandidateController(candidateService, jwtUtil);
                panelController = new PanelController(panelService, interviewService, userRepository);
        }

        /**
         * Tests all CandidateController methods.
         */
        @Test
        void candidateControllerMethodsShouldDelegate() {

                CandidateRegisterRequest register = new CandidateRegisterRequest();
                assertTrue(candidateController.register(register).getBody().contains("success"));
                verify(candidateService).register(register);

                LoginRequestDTO login = new LoginRequestDTO();
                login.setEmail("candidate@test.com");

                User user = new User();
                user.setEmail("candidate@test.com");
                user.setRole(Role.CANDIDATE);

                when(candidateService.login(login)).thenReturn(user);
                when(jwtUtil.generateAccessToken("candidate@test.com", "CANDIDATE")).thenReturn("jwt");

                assertEquals("jwt", candidateController.login(login).getBody());

                CandidateProfileRequest profile = new CandidateProfileRequest();
                CandidateResponseDTO response = new CandidateResponseDTO();
                response.setStatus(Stage.PROFILING);

                when(jwtUtil.extractEmail("token")).thenReturn("candidate@test.com");
                when(candidateService.applyToJob(eq(profile), any(), eq("candidate@test.com")))
                                .thenReturn(response);

                assertEquals(
                                Stage.PROFILING,
                                candidateController.applyToJob(
                                                profile,
                                                new MockMultipartFile("resumeFile", "r.pdf", "application/pdf",
                                                                "x".getBytes()),
                                                "Bearer token").getBody().getStatus());

                when(candidateService.getMyStatus("candidate@test.com")).thenReturn(response);

                assertEquals(
                                Stage.PROFILING,
                                candidateController.getMyStatus("Bearer token").getBody().getStatus());

                assertTrue(candidateController.logout().getBody().contains("success"));

                CandidateOnboardRequest onboard = new CandidateOnboardRequest();
                assertTrue(candidateController.onboardCandidate(onboard).getBody().contains("success"));
        }

        /**
         * Tests all PanelController methods.
         */
        @Test
        void panelControllerMethodsShouldDelegate() {

                PanelCreateRequest create = new PanelCreateRequest();
                when(panelService.createPanel(create)).thenReturn("created");
                assertEquals("created", panelController.createPanel(create));

                PanelActivationRequest activation = new PanelActivationRequest();
                when(panelService.activatePanel(activation)).thenReturn("activated");
                assertEquals("activated", panelController.activatePanel(activation));

                when(panelService.getAllPanels()).thenReturn(List.of(create));
                assertEquals(1, panelController.getAllPanels().size());

                User panel = new User();
                panel.setEmail("panel@test.com");

                when(userRepository.findByEmailIgnoreCase("panel@test.com"))
                                .thenReturn(Optional.of(panel));

                PanelInterviewDTO interview = new PanelInterviewDTO();
                interview.setInterviewId(10L);

                when(interviewService.getPanelInterviews(nullable(Long.class)))
                                .thenReturn(List.of(interview));

                UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken(
                                "panel@test.com", "x");

                assertEquals(10L, panelController.getPanelInterviews(principal).get(0).getInterviewId());
        }
}
