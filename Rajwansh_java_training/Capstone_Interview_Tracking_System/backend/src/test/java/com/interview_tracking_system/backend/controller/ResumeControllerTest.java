package com.interview_tracking_system.backend.controller;

/**
 * Static imports for assertion methods.
 */
import static org.junit.jupiter.api.Assertions.*;

/**
 * File utility import used to create test resume file.
 */
import java.nio.file.Files;

/**
 * JUnit imports used for test execution and temporary directory.
 */
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Spring imports used for HTTP status, media type and reflection test support.
 */
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * This class tests ResumeController.
 *
 * It checks whether resume files are returned correctly
 * and invalid file paths are handled properly.
 */
class ResumeControllerTest {

    /**
     * Temporary directory used to store resume files during test.
     */
    @TempDir
    java.nio.file.Path tempDir;

    /**
     * Tests resume view API for valid file, missing file and invalid path.
     *
     * @throws Exception if test file creation fails
     */
    @Test
    void viewResumeShouldReturnPdfWhenFileExistsAndHandleBadPaths() throws Exception {
        ResumeController controller = new ResumeController();

        ReflectionTestUtils.setField(
                controller,
                "resumeUploadDir",
                tempDir.toString());

        Files.writeString(
                tempDir.resolve("resume.pdf"),
                "pdf");

        var ok = controller.viewResume("resume.pdf");

        assertEquals(
                HttpStatus.OK,
                ok.getStatusCode());

        assertEquals(
                MediaType.APPLICATION_PDF,
                ok.getHeaders().getContentType());

        assertNotNull(ok.getBody());

        assertEquals(
                HttpStatus.NOT_FOUND,
                controller.viewResume("missing.pdf").getStatusCode());

        assertEquals(
                HttpStatus.BAD_REQUEST,
                controller.viewResume("../secret.pdf").getStatusCode());
    }
}
