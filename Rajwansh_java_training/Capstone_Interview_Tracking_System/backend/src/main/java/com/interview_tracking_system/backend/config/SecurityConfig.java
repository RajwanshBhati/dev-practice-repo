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

/**
 * Security configuration class for the application.
 *
 * <p>
 * Handles:
 * <ul>
 * <li>JWT authentication filter integration</li>
 * <li>Role-based authorization</li>
 * <li>CORS configuration for frontend access</li>
 * <li>Stateless session management</li>
 * </ul>
 * </p>
 */
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
                // Enable CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Disable CSRF for JWT-based authentication
                .csrf(csrf -> csrf.disable())

                // Stateless session (no session storage)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Authorization rules
                .authorizeHttpRequests(auth -> auth

                        // Public endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/jd/**").permitAll()

                        // Role-based endpoints
                        .requestMatchers("/api/hr/**").hasRole("HR")
                        .requestMatchers("/api/panel/**").hasRole("PANEL")
                        .requestMatchers("/api/candidate/**").hasRole("CANDIDATE")

                        // Any other request must be authenticated
                        .anyRequest().authenticated())

                // Authentication provider
                .authenticationProvider(authenticationProvider())

                // JWT filter before username/password authentication
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configures CORS policy for frontend integration.
     *
     * <p>
     * Allows frontend running on:
     * <ul>
     * <li>http://localhost:3000</li>
     * <li>http://127.0.0.1:5500</li>
     * </ul>
     * </p>
     *
     * @return CORS configuration source
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://127.0.0.1:5500"));

        config.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"));

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
