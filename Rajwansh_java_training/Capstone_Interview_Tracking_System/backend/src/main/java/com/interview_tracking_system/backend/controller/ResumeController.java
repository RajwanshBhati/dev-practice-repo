package com.interview_tracking_system.backend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.interview_tracking_system.backend.constants.ApiEndpoints;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Controller for handling resume viewing operations.
 * Provides endpoint to fetch and display resume files stored on the server.
 */
@RestController
@RequestMapping(ApiEndpoints.RESUMES)
public class ResumeController {

    /**
     * Directory where resume files are stored.
     */
    @Value("${resume.upload-dir:uploads/resumes}")
    private String resumeUploadDir;

    /**
     * Fetches a resume file by its name and returns it as a resource.
     *
     * @param fileName name of the resume file
     * @return resume file as a downloadable resource
     */
    @GetMapping(ApiEndpoints.RESUME_FILE)
    public ResponseEntity<Resource> viewResume(@PathVariable final String fileName) throws Exception {

        Path basePath = Paths.get(resumeUploadDir).toAbsolutePath().normalize();

        Path filePath = basePath.resolve(fileName).normalize();

        if (!filePath.startsWith(basePath)) {
            return ResponseEntity.badRequest().build();
        }

        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        String contentType = MediaType.APPLICATION_PDF_VALUE;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
