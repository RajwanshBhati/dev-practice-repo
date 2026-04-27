package com.interview_tracking_system.backend.config;

import com.interview_tracking_system.backend.entity.User;
import com.interview_tracking_system.backend.enums.Role;
import com.interview_tracking_system.backend.enums.UserStatus;
import com.interview_tracking_system.backend.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Seeds default HR user at application startup.
 */
@Component
public class DataSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.hr.email}")
    private String hrEmail;

    @Value("${app.hr.password}")
    private String hrPassword;

    @Value("${app.hr.name}")
    private String hrName;

    /**
     * Constructor injection of dependencies
     *
     * @param userRepository
     * @param passwordEncoder
     * @param userRepository
     * @param passwordEncoder
     */
    public DataSeeder(UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {

        if (userRepository.existsByEmail(hrEmail)) {
            log.info("HR user already exists - skipping seeding");
            return;
        }

        User hr = new User();
        hr.setName(hrName);
        hr.setEmail(hrEmail);
        hr.setMobile("0000000000");
        hr.setPassword(passwordEncoder.encode(hrPassword));
        hr.setRole(Role.HR);
        hr.setStatus(UserStatus.ACTIVE);

        userRepository.save(hr);

        log.info("Default HR user created with email: {}", hrEmail);
    }
}
