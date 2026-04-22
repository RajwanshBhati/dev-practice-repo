package com.interview_tracking_system.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Interview Tracking System backend application.
 */
@SpringBootApplication
public final class BackendApplication {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private BackendApplication() {
    }

    /**
     * Main method that starts the Spring Boot application.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}
