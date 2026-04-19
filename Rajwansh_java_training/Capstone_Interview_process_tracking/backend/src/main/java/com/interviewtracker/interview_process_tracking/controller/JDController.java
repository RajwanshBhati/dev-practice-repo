package com.interviewtracker.interview_process_tracking.controller;

import com.interviewtracker.interview_process_tracking.dto.ApiResponse;
import com.interviewtracker.interview_process_tracking.dto.JDRequestDTO;
import com.interviewtracker.interview_process_tracking.dto.JDResponseDTO;
import com.interviewtracker.interview_process_tracking.enums.JDStatus;
import com.interviewtracker.interview_process_tracking.enums.JobType;
import com.interviewtracker.interview_process_tracking.service.JDService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


// Here I have defined the JDController class, which is a REST controller responsible for handling HTTP requests related to job descriptions. 
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class JDController {

    private final JDService jdService;

    
    // Here I defined the createJD method Which handles POST Requests to create a new job description
    @PostMapping("/hr/jd")
    public ResponseEntity<ApiResponse<JDResponseDTO>> createJD(
            @Valid @RequestBody JDRequestDTO requestDTO) {
        JDResponseDTO response = jdService.createJD(requestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Job Description created successfully", response));
    }
    
    // Here I defined the updateJD method which handles PUT Requests to update an existing job description based on its ID. It takes the ID as a path variable and the updated job description details in the request body.
    @PutMapping("/hr/jd/{id}")
    public ResponseEntity<ApiResponse<JDResponseDTO>> updateJD(
            @PathVariable UUID id,
            @Valid @RequestBody JDRequestDTO requestDTO) {
        JDResponseDTO response = jdService.updateJD(id, requestDTO);
        return ResponseEntity.ok(ApiResponse.success("Job Description updated successfully", response));
    }
    
    // Here I defined the updateStatus method which handles PATCH Requests to update the status of a job description based on its ID. It takes the ID as a path variable and the new status as a request parameter.
    @PatchMapping("/hr/jd/{id}/status")
    public ResponseEntity<ApiResponse<JDResponseDTO>> updateStatus(
            @PathVariable UUID id,
            @RequestParam JDStatus status) {
        JDResponseDTO response = jdService.updateJDStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Status updated to " + status, response));
    }
    
    // Here I defined the deleteJD method which handles DELETE Requests to delete a job description based on its ID. It takes the ID as a path variable and returns a success message upon successful deletion.
    @DeleteMapping("/hr/jd/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteJD(@PathVariable UUID id) {
        jdService.deleteJD(id);
        return ResponseEntity.ok(ApiResponse.success("Job Description deleted successfully", null));
    }
    
    // Here I defined the getAllJDs method which handles GET Requests to retrieve all job descriptions. It returns a list of job descriptions wrapped in an ApiResponse object.
    @GetMapping("/hr/jd")
    public ResponseEntity<ApiResponse<List<JDResponseDTO>>> getAllJDs() {
        List<JDResponseDTO> list = jdService.getAllJDs();
        return ResponseEntity.ok(ApiResponse.success("Fetched all Job Descriptions", list));
    }
    

    // Here I defined the searchJDs method which handles GET Requests to search for job descriptions based on various criteria such as status, job type, location, and title.
    @GetMapping("/hr/jd/search")
    public ResponseEntity<ApiResponse<List<JDResponseDTO>>> searchJDs(
            @RequestParam(required = false) JDStatus status,
            @RequestParam(required = false) JobType jobType,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String title) {
        List<JDResponseDTO> result = jdService.searchJDs(status, jobType, location, title);
        return ResponseEntity.ok(ApiResponse.success("Search results", result));
    }

    // Here I defined the getActiveJDs method which handles GET Requests to retrieve all active job descriptions. It returns a list of active job descriptions wrapped in an ApiResponse object.
    @GetMapping("/jd")
    public ResponseEntity<ApiResponse<List<JDResponseDTO>>> getActiveJDs() {
        List<JDResponseDTO> list = jdService.getActiveJDs();
        return ResponseEntity.ok(ApiResponse.success("Active Job Descriptions", list));
    }
    
    // Here I defined the getJDById method which handles GET Requests to retrieve a specific job description based on its ID. 
    @GetMapping("/jd/{id}")
    public ResponseEntity<ApiResponse<JDResponseDTO>> getJDById(@PathVariable UUID id) {
        JDResponseDTO response = jdService.getJDById(id);
        return ResponseEntity.ok(ApiResponse.success("Job Description fetched", response));
    }
}