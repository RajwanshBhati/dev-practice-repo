package com.interview_tracking_system.backend.security;

import com.interview_tracking_system.backend.entity.CandidateUser;
import com.interview_tracking_system.backend.entity.User;
import com.interview_tracking_system.backend.repository.CandidateUserRepository;
import com.interview_tracking_system.backend.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    /** Logger for debugging authentication flow */
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);

    /** Repository for system users */
    private final UserRepository userRepository;

    /** Repository for candidate users */
    private final CandidateUserRepository candidateUserRepository;

    /**
     * Constructor for dependency injection.
     *
     * @param userRepository          repository for system users
     * @param candidateUserRepository repository for candidate users
     */
    public CustomUserDetailsService(
            final UserRepository userRepository,
            final CandidateUserRepository candidateUserRepository) {

        this.userRepository = userRepository;
        this.candidateUserRepository = candidateUserRepository;
    }

    /**
     * Loads user details by email.
     *
     * First checks in CandidateUser table.
     * If not found, then checks in User table.
     *
     * @param email user email (username)
     * @return UserDetails object for authentication
     * @throws UsernameNotFoundException if user not found
     */
    @Override
    public UserDetails loadUserByUsername(final String email)
            throws UsernameNotFoundException {

        LOGGER.info("Loading user for email: {}", email);

        // Check Candidate User
        CandidateUser candidateUser = candidateUserRepository.findByEmail(email).orElse(null);

        if (candidateUser != null) {

            LOGGER.info("Candidate user found: {}", email);

            return new org.springframework.security.core.userdetails.User(
                    candidateUser.getEmail(),
                    candidateUser.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_CANDIDATE")));
        }

        // Check System User (HR / Panel)
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    LOGGER.error("User not found: {}", email);
                    return new UsernameNotFoundException("User not found");
                });

        LOGGER.info("System user found: {} with role: {}", email, user.getRole());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
    }
}
