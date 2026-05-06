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

/**
 * Loads application users for Spring Security authentication.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    /** Logger for authentication flow. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);

    /** Repository for all users. */
    private final UserRepository userRepository;

    /**
     * Creates CustomUserDetailsService with user repository.
     *
     * @param repository user repository
     */
    public CustomUserDetailsService(final UserRepository repository) {
        this.userRepository = repository;
    }

    /**
     * Loads user details by email.
     *
     * @param email user email
     * @return user details for authentication
     * @throws UsernameNotFoundException when user is not found
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
