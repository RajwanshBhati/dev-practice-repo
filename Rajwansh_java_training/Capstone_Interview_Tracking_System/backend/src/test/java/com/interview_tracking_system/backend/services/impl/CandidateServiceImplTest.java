package com.interview_tracking_system.backend.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interview_tracking_system.backend.dto.CandidateOnboardRequest;
import com.interview_tracking_system.backend.dto.CandidateProfileRequest;
import com.interview_tracking_system.backend.dto.CandidateRegisterRequest;
import com.interview_tracking_system.backend.dto.CandidateResponseDTO;
import com.interview_tracking_system.backend.dto.LoginRequestDTO;
import com.interview_tracking_system.backend.entity.Candidate;
import com.interview_tracking_system.backend.entity.User;
import com.interview_tracking_system.backend.enums.Gender;
import com.interview_tracking_system.backend.enums.Role;
import com.interview_tracking_system.backend.enums.Stage;
import com.interview_tracking_system.backend.enums.UserStatus;
import com.interview_tracking_system.backend.repository.CandidateRepository;
import com.interview_tracking_system.backend.repository.UserRepository;
import com.interview_tracking_system.backend.service.EmailService;
import com.interview_tracking_system.backend.service.impl.CandidateServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Unit test class for CandidateServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class CandidateServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @TempDir
    private java.nio.file.Path tempDir;

    private CandidateServiceImpl service;

    /**
     * Initializes service before each test.
     */
    @BeforeEach
    void setUp() {
        service = new CandidateServiceImpl(
                userRepository,
                candidateRepository,
                emailService,
                passwordEncoder);

        ReflectionTestUtils.setField(service, "resumeUploadDir", tempDir.toString());
    }

    /**
     * Tests successful candidate registration.
     */
    @Test
    void shouldRegisterCandidateSuccessfully() {
        CandidateRegisterRequest request = createRegisterRequest();

        when(userRepository.existsByEmail("raj@test.com")).thenReturn(false);
        when(userRepository.existsByMobile("9876543210")).thenReturn(false);

        service.register(request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        verify(emailService).sendCandidateRegistrationEmail(
                any(), any(), any());

        assertEquals("Raj", captor.getValue().getName());
        assertEquals("raj@test.com", captor.getValue().getEmail());
        assertEquals(Role.CANDIDATE, captor.getValue().getRole());
        assertEquals(UserStatus.ACTIVE, captor.getValue().getStatus());
    }

    /**
     * Tests registration failure for missing fields.
     */
    @Test
    void shouldThrowExceptionWhenRegisterFieldsAreMissing() {
        CandidateRegisterRequest request = new CandidateRegisterRequest();

        assertThrows(IllegalArgumentException.class, () -> service.register(request));
    }

    /**
     * Tests registration failure when email already exists.
     */
    @Test
    void shouldThrowExceptionWhenRegisterEmailExists() {
        CandidateRegisterRequest request = createRegisterRequest();

        when(userRepository.existsByEmail("raj@test.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.register(request));
    }

    /**
     * Tests registration failure when mobile already exists.
     */
    @Test
    void shouldThrowExceptionWhenRegisterMobileExists() {
        CandidateRegisterRequest request = createRegisterRequest();

        when(userRepository.existsByEmail("raj@test.com")).thenReturn(false);
        when(userRepository.existsByMobile("9876543210")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.register(request));
    }

    /**
     * Tests successful candidate login.
     */
    @Test
    void shouldLoginCandidateSuccessfully() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("raj@test.com");
        request.setPassword("password");

        User user = new User();
        user.setEmail("raj@test.com");
        user.setPassword("encoded");

        when(userRepository.findByEmailIgnoreCase("raj@test.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encoded")).thenReturn(true);

        User result = service.login(request);

        assertEquals("raj@test.com", result.getEmail());
    }

    /**
     * Tests login failure when user is not found.
     */
    @Test
    void shouldThrowExceptionWhenLoginUserNotFound() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("missing@test.com");

        when(userRepository.findByEmailIgnoreCase("missing@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.login(request));
    }

    /**
     * Tests login failure when password is invalid.
     */
    @Test
    void shouldThrowExceptionWhenLoginPasswordInvalid() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("raj@test.com");
        request.setPassword("wrong");

        User user = new User();
        user.setPassword("encoded");

        when(userRepository.findByEmailIgnoreCase("raj@test.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encoded")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> service.login(request));
    }

    /**
     * Tests successful job application.
     */
    @Test
    void shouldApplyToJobSuccessfully() {
        CandidateProfileRequest request = createProfileRequest();
        MockMultipartFile resume = new MockMultipartFile(
                "resumeFile",
                "resume.pdf",
                "application/pdf",
                "PDF".getBytes());

        User user = new User();
        user.setEmail("raj@test.com");

        Candidate savedCandidate = createCandidate(Stage.PROFILING);

        when(userRepository.findByEmailIgnoreCase("raj@test.com"))
                .thenReturn(Optional.of(user));
        when(candidateRepository.findTopByEmailIgnoreCaseOrderByIdDesc("raj@test.com"))
                .thenReturn(Optional.empty());
        when(candidateRepository.findTopByMobileOrderByIdDesc("+919876543210"))
                .thenReturn(Optional.empty());
        when(candidateRepository.save(any(Candidate.class)))
                .thenReturn(savedCandidate);

        CandidateResponseDTO response = service.applyToJob(
                request,
                resume,
                "raj@test.com");

        assertEquals(Stage.PROFILING, response.getStatus());
        verify(emailService).sendProfilingCompletedEmail("raj@test.com", "Raj");
    }

    /**
     * Tests apply job failure when user is not logged in.
     */
    @Test
    void shouldThrowExceptionWhenApplyUserNotLoggedIn() {
        CandidateProfileRequest request = createProfileRequest();

        when(userRepository.findByEmailIgnoreCase("raj@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> service.applyToJob(request, null, "raj@test.com"));
    }

    /**
     * Tests apply job failure for invalid mobile number.
     */
    @Test
    void shouldThrowExceptionWhenApplyMobileInvalid() {
        CandidateProfileRequest request = createProfileRequest();
        request.setMobileNumber("123");

        when(userRepository.findByEmailIgnoreCase("raj@test.com"))
                .thenReturn(Optional.of(new User()));

        assertThrows(IllegalArgumentException.class,
                () -> service.applyToJob(request, null, "raj@test.com"));
    }

    /**
     * Tests apply job failure when active application already exists.
     */
    @Test
    void shouldThrowExceptionWhenCandidateAlreadyApplied() {
        CandidateProfileRequest request = createProfileRequest();
        Candidate existingCandidate = createCandidate(Stage.PROFILING);

        when(userRepository.findByEmailIgnoreCase("raj@test.com"))
                .thenReturn(Optional.of(new User()));
        when(candidateRepository.findTopByEmailIgnoreCaseOrderByIdDesc("raj@test.com"))
                .thenReturn(Optional.of(existingCandidate));
        when(candidateRepository.findTopByMobileOrderByIdDesc("+919876543210"))
                .thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> service.applyToJob(request, null, "raj@test.com"));
    }

    /**
     * Tests apply job failure when jd id is missing.
     */
    @Test
    void shouldThrowExceptionWhenJdIdMissing() {
        CandidateProfileRequest request = createProfileRequest();
        request.setJdId(null);

        when(userRepository.findByEmailIgnoreCase("raj@test.com"))
                .thenReturn(Optional.of(new User()));
        when(candidateRepository.findTopByEmailIgnoreCaseOrderByIdDesc("raj@test.com"))
                .thenReturn(Optional.empty());
        when(candidateRepository.findTopByMobileOrderByIdDesc("+919876543210"))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> service.applyToJob(request, null, "raj@test.com"));
    }

    /**
     * Tests status response when candidate has no application.
     */
    @Test
    void shouldReturnNotAppliedWhenCandidateDoesNotExist() {
        when(candidateRepository.findTopByEmailIgnoreCaseOrderByIdDesc("raj@test.com"))
                .thenReturn(Optional.empty());

        CandidateResponseDTO response = service.getMyStatus("raj@test.com");

        assertEquals(Stage.NOT_APPLIED, response.getStatus());
    }

    /**
     * Tests status response when candidate application exists.
     */
    @Test
    void shouldReturnCandidateStatusWhenCandidateExists() {
        Candidate candidate = createCandidate(Stage.SCREENING);

        when(candidateRepository.findTopByEmailIgnoreCaseOrderByIdDesc("raj@test.com"))
                .thenReturn(Optional.of(candidate));

        CandidateResponseDTO response = service.getMyStatus("raj@test.com");

        assertEquals(Stage.SCREENING, response.getStatus());
        assertEquals("raj@test.com", response.getEmail());
    }

    /**
     * Tests successful HR candidate onboarding.
     */
    @Test
    void shouldOnboardCandidateSuccessfully() {
        CandidateOnboardRequest request = createOnboardRequest();

        when(userRepository.existsByEmail("raj@test.com")).thenReturn(false);
        when(userRepository.existsByMobile("9876543210")).thenReturn(false);

        service.onboardCandidate(request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        verify(emailService).sendCandidateOnboardEmail(any(), any(), any());

        assertEquals(UserStatus.PENDING, captor.getValue().getStatus());
        assertEquals(Role.CANDIDATE, captor.getValue().getRole());
    }

    /**
     * Tests onboarding failure for missing fields.
     */
    @Test
    void shouldThrowExceptionWhenOnboardFieldsAreMissing() {
        CandidateOnboardRequest request = new CandidateOnboardRequest();

        assertThrows(IllegalArgumentException.class, () -> service.onboardCandidate(request));
    }

    /**
     * Creates registration request test data.
     *
     * @return candidate registration request
     */
    private CandidateRegisterRequest createRegisterRequest() {
        CandidateRegisterRequest request = new CandidateRegisterRequest();
        request.setFullName(" Raj ");
        request.setEmail(" Raj@Test.COM ");
        request.setMobileNumber("9876543210");
        request.setDob(LocalDate.of(2000, 1, 1));
        request.setGender(Gender.MALE);
        return request;
    }

    /**
     * Creates profile request test data.
     *
     * @return candidate profile request
     */
    private CandidateProfileRequest createProfileRequest() {
        CandidateProfileRequest request = new CandidateProfileRequest();
        request.setName("Raj");
        request.setMobileCode("+91");
        request.setMobileNumber("9876543210");
        request.setDateOfBirth(LocalDate.of(2000, 1, 1));
        request.setCurrentCompany("ABC");
        request.setTotalExp(5);
        request.setRelevantExp(4);
        request.setCurrentCtc(new BigDecimal("5.5"));
        request.setExpectedCtc(new BigDecimal("8.0"));
        request.setNoticePeriod(30);
        request.setPreferredLocation("Pune");
        request.setSource("LinkedIn");
        request.setJdId(UUID.randomUUID());
        return request;
    }

    /**
     * Creates candidate test data.
     *
     * @param stage candidate stage
     * @return candidate entity
     */
    private Candidate createCandidate(Stage stage) {
        Candidate candidate = new Candidate();
        candidate.setId(1L);
        candidate.setName("Raj");
        candidate.setEmail("raj@test.com");
        candidate.setStatus(stage);
        candidate.setJdId(UUID.randomUUID());
        return candidate;
    }

    /**
     * Creates onboarding request test data.
     *
     * @return candidate onboarding request
     */
    private CandidateOnboardRequest createOnboardRequest() {
        CandidateOnboardRequest request = new CandidateOnboardRequest();
        request.setFullName(" Raj ");
        request.setEmail(" Raj@Test.COM ");
        request.setMobileNumber("9876543210");
        request.setDob(LocalDate.of(2000, 1, 1));
        request.setGender(Gender.MALE);
        return request;
    }
}
