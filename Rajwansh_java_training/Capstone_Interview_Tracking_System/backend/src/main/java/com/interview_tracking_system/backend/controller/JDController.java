package com.interview_tracking_system.backend.controller;

import com.interview_tracking_system.backend.dto.ApiResponse;
import com.interview_tracking_system.backend.dto.JDRequestDTO;
import com.interview_tracking_system.backend.dto.JDResponseDTO;
import com.interview_tracking_system.backend.enums.JDStatus;
import com.interview_tracking_system.backend.enums.JobType;
import com.interview_tracking_system.backend.service.JDService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class JDController {

    private static final Logger log = Logger.getLogger(JDController.class.getName());

    private final JDService jdService;

    /**
     * Constructor injection (no Lombok used)
     */
    public JDController(JDService jdService) {
        this.jdService = jdService;
    }

    /**
     * Create a new Job Description.
     *
     * @param requestDTO
     * @return
     */

    @PostMapping("/hr/jd")
    public ResponseEntity<ApiResponse<JDResponseDTO>> createJD(
            @Valid @RequestBody JDRequestDTO requestDTO) {

        log.info("Creating JD: " + requestDTO.getJobTitle());

        JDResponseDTO response = jdService.createJD(requestDTO);

        return new ResponseEntity<>(
                ApiResponse.success("Job Description created successfully", response),
                HttpStatus.CREATED);
    }

    /**
     * Get Job Description by ID.
     *
     * @param id
     * @param requestDTO
     * @return
     */
    @PutMapping("/hr/jd/{id}")
    public ResponseEntity<ApiResponse<JDResponseDTO>> updateJD(
            @PathVariable UUID id,
            @Valid @RequestBody JDRequestDTO requestDTO) {

        log.info("Updating JD: " + id);

        JDResponseDTO response = jdService.updateJD(id, requestDTO);

        return ResponseEntity.ok(
                ApiResponse.success("Job Description updated successfully", response));
    }

    /**
     * Update JD status (Active/Inactive).
     *
     * @param id
     * @param status
     * @return
     */
    @PatchMapping("/hr/jd/{id}/status")
    public ResponseEntity<ApiResponse<JDResponseDTO>> updateStatus(
            @PathVariable UUID id,
            @RequestParam JDStatus status) {

        log.info("Updating JD status: " + id + " -> " + status);

        JDResponseDTO response = jdService.updateJDStatus(id, status);

        return ResponseEntity.ok(
                ApiResponse.success("Status updated to " + status, response));
    }

    /**
     * Delete a Job Description by ID.
     *
     * @param id
     * @return
     */
    @DeleteMapping("/hr/jd/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteJD(@PathVariable UUID id) {

        log.info("Deleting JD: " + id);

        jdService.deleteJD(id);

        return ResponseEntity.ok(
                ApiResponse.success("Job Description deleted successfully", null));
    }

    /**
     * Get all Job Descriptions.
     *
     * @return List of JDs
     */
    @GetMapping("/hr/jd")
    public ResponseEntity<ApiResponse<List<JDResponseDTO>>> getAllJDs() {

        log.info("Fetching all JDs");

        List<JDResponseDTO> list = jdService.getAllJDs();

        return ResponseEntity.ok(
                ApiResponse.success("Fetched all Job Descriptions", list));
    }

    /**
     * Search Job Descriptions based on criteria.
     *
     * @param status
     * @param jobType
     * @param location
     * @param title
     * @return
     */
    @GetMapping("/hr/jd/search")
    public ResponseEntity<ApiResponse<List<JDResponseDTO>>> searchJDs(
            @RequestParam(required = false) JDStatus status,
            @RequestParam(required = false) JobType jobType,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String title) {

        log.info("Searching JDs");

        List<JDResponseDTO> result = jdService.searchJDs(status, jobType, location, title);

        return ResponseEntity.ok(
                ApiResponse.success("Search results", result));
    }

    /**
     * Get all active Job Descriptions (for candidates).
     *
     * @return List of active JDs
     */

    @GetMapping("/jd")
    public ResponseEntity<ApiResponse<List<JDResponseDTO>>> getActiveJDs() {

        log.info("Fetching active JDs");

        List<JDResponseDTO> list = jdService.getActiveJDs();

        return ResponseEntity.ok(
                ApiResponse.success("Active Job Descriptions", list));
    }

    @GetMapping("/jd/{id}")
    public ResponseEntity<ApiResponse<JDResponseDTO>> getJDById(
            @PathVariable UUID id) {

        log.info("Fetching JD by ID: " + id);

        JDResponseDTO response = jdService.getJDById(id);

        return ResponseEntity.ok(
                ApiResponse.success("Job Description fetched", response));
    }
}
