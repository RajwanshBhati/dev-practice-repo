package com.interview_tracking_system.backend.controller;

import com.interview_tracking_system.backend.constants.ApiEndpoints;
import com.interview_tracking_system.backend.dto.ApiResponse;
import com.interview_tracking_system.backend.dto.JDRequestDTO;
import com.interview_tracking_system.backend.dto.JDResponseDTO;
import com.interview_tracking_system.backend.enums.JDStatus;
import com.interview_tracking_system.backend.enums.JobType;
import com.interview_tracking_system.backend.service.JDService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Job Descriptions.
 */
@RestController
@RequestMapping(ApiEndpoints.BASE_API)
public class JDController {

        /** Logger instance. */
        private static final Logger LOGGER = LoggerFactory.getLogger(JDController.class);

        /** Job description service. */
        private final JDService jdService;

        /**
         * Constructs JDController.
         *
         * @param injectedJdService the job description service
         */
        @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Spring dependency injection stores"
                        + " framework-managed beans.")
        public JDController(final JDService injectedJdService) {
                this.jdService = injectedJdService;
        }

        /**
         * Creates a new Job Description.
         *
         * @param requestDTO the job description request
         * @return the created job description
         */
        @PostMapping(ApiEndpoints.HR_JD)
        public ResponseEntity<ApiResponse<JDResponseDTO>> createJD(
                        @Valid @RequestBody final JDRequestDTO requestDTO) {

                LOGGER.info("Creating JD: {}", requestDTO.getJobTitle());

                JDResponseDTO response = jdService.createJD(requestDTO);

                return new ResponseEntity<>(
                                ApiResponse.success(
                                                "Job Description created successfully",
                                                response),
                                HttpStatus.CREATED);
        }

        /**
         * Updates a Job Description.
         *
         * @param jdId       the job description id
         * @param requestDTO the request data
         * @return the updated job description
         */
        @PutMapping(ApiEndpoints.HR_JD + "/{id}")
        public ResponseEntity<ApiResponse<JDResponseDTO>> updateJD(
                        @PathVariable("id") final UUID jdId,
                        @Valid @RequestBody final JDRequestDTO requestDTO) {

                LOGGER.info("Updating JD: {}", jdId);

                JDResponseDTO response = jdService.updateJD(jdId, requestDTO);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                "Job Description updated successfully",
                                                response));
        }

        /**
         * Deletes a Job Description.
         *
         * @param jdId the job description id
         * @return success response
         */
        @DeleteMapping(ApiEndpoints.HR_JD + "/{id}")
        public ResponseEntity<ApiResponse<Void>> deleteJD(
                        @PathVariable("id") final UUID jdId) {

                LOGGER.info("Deleting JD: {}", jdId);

                jdService.deleteJD(jdId);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                "Job Description deleted successfully",
                                                null));
        }

        /**
         * Retrieves all Job Descriptions.
         *
         * @return list of job descriptions
         */
        @GetMapping(ApiEndpoints.HR_JD)
        public ResponseEntity<ApiResponse<List<JDResponseDTO>>> getAllJDs() {

                LOGGER.info("Fetching all JDs");

                List<JDResponseDTO> list = jdService.getAllJDs();

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                "Fetched all Job Descriptions",
                                                list));
        }

        /**
         * Searches Job Descriptions.
         *
         * @param status   the job status
         * @param jobType  the job type
         * @param location the job location
         * @param title    the job title
         * @return search results
         */
        @GetMapping(ApiEndpoints.HR_JD + ApiEndpoints.JD_SEARCH)
        public ResponseEntity<ApiResponse<List<JDResponseDTO>>> searchJDs(
                        @RequestParam(required = false) final JDStatus status,
                        @RequestParam(required = false) final JobType jobType,
                        @RequestParam(required = false) final String location,
                        @RequestParam(required = false) final String title) {

                LOGGER.info("Searching JDs");

                List<JDResponseDTO> result = jdService.searchJDs(status, jobType, location, title);

                return ResponseEntity.ok(
                                ApiResponse.success("Search results", result));
        }

        /**
         * Retrieves active Job Descriptions.
         *
         * @return list of active job descriptions
         */
        @GetMapping(ApiEndpoints.JD)
        public ResponseEntity<ApiResponse<List<JDResponseDTO>>> getActiveJDs() {

                LOGGER.info("Fetching active JDs");

                List<JDResponseDTO> list = jdService.getActiveJDs();

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                "Active Job Descriptions",
                                                list));
        }

        /**
         * Retrieves a Job Description by id.
         *
         * @param jdId the job description id
         * @return the job description
         */
        @GetMapping(ApiEndpoints.JD + "/{id}")
        public ResponseEntity<ApiResponse<JDResponseDTO>> getJDById(
                        @PathVariable("id") final UUID jdId) {

                LOGGER.info("Fetching JD by ID: {}", jdId);

                JDResponseDTO response = jdService.getJDById(jdId);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                "Job Description fetched",
                                                response));
        }

        /**
         * Updates job status.
         *
         * @param jdId   the job description id
         * @param status the new status
         * @return updated job description
         */
        @PatchMapping(ApiEndpoints.HR_JD + "/{id}/status")
        public ResponseEntity<ApiResponse<JDResponseDTO>> updateStatus(
                        @PathVariable("id") final UUID jdId,
                        @RequestParam final String status) {

                JDStatus jdStatus = JDStatus.valueOf(status.toUpperCase());

                LOGGER.info("Updating JD status: {} -> {}", jdId, jdStatus);

                JDResponseDTO response = jdService.updateJDStatus(jdId, jdStatus);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                "Status updated to " + jdStatus,
                                                response));
        }
}
