package com.interview_tracking_system.backend.security;

import com.interview_tracking_system.backend.entity.User;
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

    /** Repository for all users (HR, Panel, Candidate) */
    private final UserRepository userRepository;

    /**
     * Constructor for dependency injection.
     *
     * @param userRepository repository for all users
     */
    public CustomUserDetailsService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads user details by email (case-insensitive).
     *
     * Supports all user types: HR, Panel, and Candidate from single users table.
     *
     * @param email user email (username)
     * @return UserDetails object for authentication with assigned role
     * @throws UsernameNotFoundException if user not found
     */
    @Override
    public UserDetails loadUserByUsername(final String email)
            throws UsernameNotFoundException {

        LOGGER.info("Loading user for email: {}", email);

        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> {
                    LOGGER.error("User not found: {}", email);
                    return new UsernameNotFoundException("User not found");
                });

        LOGGER.info("User found: {} with role: {}", email, user.getRole());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
    }
}
