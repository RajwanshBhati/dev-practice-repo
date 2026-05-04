package com.interview_tracking_system.backend.config;

import com.interview_tracking_system.backend.entity.User;
import com.interview_tracking_system.backend.enums.Role;
import com.interview_tracking_system.backend.enums.UserStatus;
import com.interview_tracking_system.backend.repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.security.MessageDigest;

/**
 * Seeds default HR user at application startup.
 */
@Component
public class DataSeeder implements ApplicationRunner {

    /**
     * Logger instance.
     */
    private static final Logger LOG = LoggerFactory.getLogger(DataSeeder.class);

    /**
     * User repository.
     */
    private final UserRepository userRepository;

    /**
     * Password encoder.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Default HR email.
     */
    @Value("${app.hr.email}")
    private String hrEmail;

    /**
     * Default HR password.
     */
    @Value("${app.hr.password}")
    private String hrPassword;

    /**
     * Default HR name.
     */
    @Value("${app.hr.name}")
    private String hrName;

    /**
     * Constructor injection of dependencies.
     *
     * @param userRepository  the user repository
     * @param passwordEncoder the password encoder
     */
    public DataSeeder(final UserRepository userRepository,
            final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private String sha256Hex(final String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder hex = new StringBuilder();

            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }

            return hex.toString();
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 algorithm not available", exception);
        }
    }

    /**
     * Seeds default HR user if not already present.
     *
     * @param args application arguments
     */
    @Override
    public void run(final ApplicationArguments args) {

        if (userRepository.existsByEmail(hrEmail)) {
            LOG.info("HR user already exists - skipping seeding");
            return;
        }

        User hr = new User();
        hr.setName(hrName);
        hr.setEmail(hrEmail);
        hr.setMobile("0000000000");
        hr.setPassword(passwordEncoder.encode(sha256Hex(hrPassword)));
        hr.setRole(Role.HR);
        hr.setStatus(UserStatus.ACTIVE);

        userRepository.save(hr);

        LOG.info("Default HR user created with email: {}", hrEmail);
    }
}
