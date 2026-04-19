package com.interviewtracker.interview_process_tracking.controller;

import com.interviewtracker.interview_process_tracking.dto.ApiResponse;
import com.interviewtracker.interview_process_tracking.dto.JDRequestDTO;
import com.interviewtracker.interview_process_tracking.dto.JDResponseDTO;
import com.interviewtracker.interview_process_tracking.enum.JDStatus;
import com.interviewtracker.interview_process_tracking.enum.JobType;
import com.interviewtracker.interview_process_tracking.service.JDService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class JDController {

    private final JDService jdService;


    @PostMapping("/hr/jd")
    public ResponseEntity<ApiResponse<JDResponseDTO>> createJD(
            @Valid @RequestBody JDRequestDTO requestDTO) {
        JDResponseDTO response = jdService.createJD(requestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Job Description created successfully", response));
    }

    @PutMapping("/hr/jd/{id}")
    public ResponseEntity<ApiResponse<JDResponseDTO>> updateJD(
            @PathVariable UUID id,
            @Valid @RequestBody JDRequestDTO requestDTO) {
        JDResponseDTO response = jdService.updateJD(id, requestDTO);
        return ResponseEntity.ok(ApiResponse.success("Job Description updated successfully", response));
    }

    @PatchMapping("/hr/jd/{id}/status")
    public ResponseEntity<ApiResponse<JDResponseDTO>> updateStatus(
            @PathVariable UUID id,
            @RequestParam JDStatus status) {
        JDResponseDTO response = jdService.updateJDStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Status updated to " + status, response));
    }

    @DeleteMapping("/hr/jd/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteJD(@PathVariable UUID id) {
        jdService.deleteJD(id);
        return ResponseEntity.ok(ApiResponse.success("Job Description deleted successfully", null));
    }

    @GetMapping("/hr/jd")
    public ResponseEntity<ApiResponse<List<JDResponseDTO>>> getAllJDs() {
        List<JDResponseDTO> list = jdService.getAllJDs();
        return ResponseEntity.ok(ApiResponse.success("Fetched all Job Descriptions", list));
    }

    @GetMapping("/hr/jd/search")
    public ResponseEntity<ApiResponse<List<JDResponseDTO>>> searchJDs(
            @RequestParam(required = false) JDStatus status,
            @RequestParam(required = false) JobType jobType,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String title) {
        List<JDResponseDTO> result = jdService.searchJDs(status, jobType, location, title);
        return ResponseEntity.ok(ApiResponse.success("Search results", result));
    }


    @GetMapping("/jd")
    public ResponseEntity<ApiResponse<List<JDResponseDTO>>> getActiveJDs() {
        List<JDResponseDTO> list = jdService.getActiveJDs();
        return ResponseEntity.ok(ApiResponse.success("Active Job Descriptions", list));
    }

    @GetMapping("/jd/{id}")
    public ResponseEntity<ApiResponse<JDResponseDTO>> getJDById(@PathVariable UUID id) {
        JDResponseDTO response = jdService.getJDById(id);
        return ResponseEntity.ok(ApiResponse.success("Job Description fetched", response));
    }
}