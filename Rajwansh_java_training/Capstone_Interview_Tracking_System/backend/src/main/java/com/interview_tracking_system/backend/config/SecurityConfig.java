package com.interview_tracking_system.backend.config;

import com.interview_tracking_system.backend.security.CustomUserDetailsService;
import com.interview_tracking_system.backend.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    /**
     * Constructor injection for required security dependencies.
     *
     * @param jwtAuthFilter      JWT authentication filter
     * @param userDetailsService custom user details service
     */
    public SecurityConfig(JwtAuthFilter jwtAuthFilter,
            CustomUserDetailsService userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Configures the Spring Security filter chain.
     *
     * @param http HttpSecurity configuration object
     * @return configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())

                /**
                 * Set session management to stateless since we're using JWT for authentication.
                 */
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        /**
                         * Authentication endpoints are public.
                         * Users must be able to register and login without a token.
                         */
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/resumes/**").permitAll()

                        /**
                         * Panel activation is triggered via email link.
                         * At this point, panel users are not authenticated yet.
                         */
                        .requestMatchers("/api/v1/panel/activate").permitAll()

                        /**
                         * Panel creation is restricted to HR users.
                         * Only HR should onboard panel members into the system.
                         */
                        .requestMatchers("/api/v1/panel/create").hasRole("HR")

                        /**
                         * Interview scheduling and candidate status updates are controlled by HR.
                         * This ensures the recruitment workflow is managed centrally.
                         */
                        .requestMatchers("/api/interview/schedule").hasRole("HR")
                        .requestMatchers("/api/interview/status").hasAnyAuthority("HR", "ROLE_HR")

                        /**
                         * Panel users can only:
                         * 1. View interviews assigned to them
                         * 2. Submit feedback for those interviews
                         * They should not have access to HR-level operations.
                         */
                        .requestMatchers("/api/interview/feedback/**").hasRole("PANEL")
                        .requestMatchers("/api/interview/panel/**").hasRole("PANEL")
                        .requestMatchers(
                                "/api/candidates/register",
                                "/api/candidates/login")
                        .permitAll()
                        /**
                         * Candidates can only track their own interview progress and status.
                         * They are not allowed to access feedback or administrative actions.
                         */
                        .requestMatchers("/api/interview/candidate/**").hasRole("CANDIDATE")

                        /**
                         * Existing role-based APIs for each module.
                         */
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/hr/jd").permitAll()
                        // .requestMatchers("/api/hr/**").hasRole("HR")
                        // .requestMatchers("/api/panel/**").hasRole("PANEL")
                        // .requestMatchers("/api/candidates/**").hasRole("CANDIDATE")

                        .requestMatchers("/api/hr/**").hasAnyAuthority("HR", "ROLE_HR")
                        .requestMatchers("/api/panel/**").hasAnyAuthority("PANEL", "ROLE_PANEL")
                        .requestMatchers("/api/candidates/onboard").hasAnyAuthority("HR", "ROLE_HR")
                        .requestMatchers("/api/candidates/**").hasAnyAuthority("CANDIDATE", "ROLE_CANDIDATE")

                        /**
                         * Any other request must be authenticated.
                         */
                        .anyRequest().authenticated())

                /* Add JWT filter before username/password authentication */
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://127.0.0.1:5500",
                "http://127.0.0.1:5501",
                "http://localhost:5501"));

        config.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    /**
     * Password encoder bean using BCrypt hashing algorithm.
     *
     * @return BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Authentication provider for user authentication.
     *
     * @return DaoAuthenticationProvider configured with user details service
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    /**
     * Provides AuthenticationManager for authentication process.
     *
     * @param config Authentication configuration
     * @return AuthenticationManager instance
     * @throws Exception if authentication manager cannot be created
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
