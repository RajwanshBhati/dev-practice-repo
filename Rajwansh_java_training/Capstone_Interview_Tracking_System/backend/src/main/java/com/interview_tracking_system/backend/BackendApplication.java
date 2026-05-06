// package com.interview_tracking_system.backend;

// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;

// /**
//  * Main Spring Boot application class.
//  */
// @SpringBootApplication
// public final class BackendApplication {

//     /**
//      * Private constructor to prevent instantiation.
//      */
//     private BackendApplication() {
//     }

//     /**
//      * Main method to start the Spring Boot application.
//      *
//      * @param args command line arguments
//      */
//     public static void main(final String[] args) {
//         SpringApplication.run(BackendApplication.class, args);
//     }
// }

package com.interview_tracking_system.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application class.
 */
@SpringBootApplication
public class BackendApplication {

    /**
     * Main method to start the Spring Boot application.
     *
     * @param args command line arguments
     */

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}
