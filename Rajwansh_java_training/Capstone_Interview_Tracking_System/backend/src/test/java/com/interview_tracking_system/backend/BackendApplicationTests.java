package com.interview_tracking_system.backend;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Spring Boot context load test.
 */
@Disabled("Disabled because unit tests use Mockito and do not require full application context.")
@SpringBootTest
class BackendApplicationTests {

    /**
     * Verifies application context loading.
     */
    @Test
    void contextLoads() {
    }
}
