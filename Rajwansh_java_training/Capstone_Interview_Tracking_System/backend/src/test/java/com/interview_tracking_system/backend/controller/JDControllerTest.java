package com.interview_tracking_system.backend.controller;

/**
 * Static imports for assertions and Mockito methods.
 */
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * DTO imports used for JD controller testing.
 */
import com.interview_tracking_system.backend.dto.ApiResponse;
import com.interview_tracking_system.backend.dto.JDRequestDTO;
import com.interview_tracking_system.backend.dto.JDResponseDTO;

/**
 * Enum imports used for JD status and job type.
 */
import com.interview_tracking_system.backend.enums.JDStatus;
import com.interview_tracking_system.backend.enums.JobType;

/**
 * Service import used to mock JD service behavior.
 */
import com.interview_tracking_system.backend.service.JDService;

/**
 * Java utility imports.
 */
import java.util.List;
import java.util.UUID;

/**
 * JUnit imports used for test setup and test methods.
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
 * Spring imports used for response status validation.
 */
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * This class tests JDController.
 *
 * It checks whether JD controller methods call JDService
 * and return proper API responses.
 */
@ExtendWith(MockitoExtension.class)
class JDControllerTest {

    /**
     * Mocked JD service.
     */
    @Mock
    private JDService service;

    /**
     * Controller under test.
     */
    private JDController controller;

    /**
     * Common JD id used in tests.
     */
    private UUID id;

    /**
     * Common JD response used in tests.
     */
    private JDResponseDTO dto;

    /**
     * Initializes test data before each test.
     */
    @BeforeEach
    void setUp() {
        controller = new JDController(service);
        id = UUID.randomUUID();

        dto = new JDResponseDTO();
        dto.setId(id);
        dto.setJobTitle("Java");
        dto.setStatus(JDStatus.ACTIVE);
    }

    /**
     * Tests all JDController methods.
     */
    @Test
    void allJDControllerMethodsShouldReturnApiResponse() {
        when(service.createJD(any())).thenReturn(dto);
        when(service.updateJD(eq(id), any())).thenReturn(dto);
        when(service.getAllJDs()).thenReturn(List.of(dto));
        when(service.searchJDs(JDStatus.ACTIVE, JobType.FULL_TIME, "Pune", "Java"))
                .thenReturn(List.of(dto));
        when(service.getActiveJDs()).thenReturn(List.of(dto));
        when(service.getJDById(id)).thenReturn(dto);
        when(service.updateJDStatus(id, JDStatus.CLOSED)).thenReturn(dto);

        JDRequestDTO request = new JDRequestDTO();
        request.setJobTitle("Java");

        assertEquals(
                HttpStatus.CREATED,
                controller.createJD(request).getStatusCode());

        assertTrue(
                controller.updateJD(id, request).getBody().isSuccess());

        assertTrue(
                controller.deleteJD(id).getBody().isSuccess());

        assertEquals(
                1,
                controller.getAllJDs().getBody().getData().size());

        assertEquals(
                1,
                controller.searchJDs(JDStatus.ACTIVE, JobType.FULL_TIME, "Pune", "Java")
                        .getBody()
                        .getData()
                        .size());

        assertEquals(
                1,
                controller.getActiveJDs().getBody().getData().size());

        assertEquals(
                id,
                controller.getJDById(id).getBody().getData().getId());

        ResponseEntity<ApiResponse<JDResponseDTO>> status = controller.updateStatus(id, "closed");

        assertTrue(
                status.getBody().getMessage().contains("CLOSED"));

        verify(service).deleteJD(id);
    }
}
