package com.interview_tracking_system.backend.config;

import com.interview_tracking_system.backend.security.CustomUserDetailsService;
import com.interview_tracking_system.backend.security.JwtAuthFilter;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;
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

/**
 * Security configuration for the application.
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    /** JWT authentication filter. */
    private final JwtAuthFilter jwtAuthFilter;

    /** Custom user details service. */
    private final CustomUserDetailsService userDetailsService;

    /**
     * Constructor injection for required security dependencies.
     *
     * @param jwtAuthFilter      JWT authentication filter
     * @param userDetailsService custom user details service
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Spring dependency injection stores framework-managed beans.")
    public SecurityConfig(final JwtAuthFilter jwtAuthFilter,
            final CustomUserDetailsService userDetailsService) {
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
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/resumes/**").permitAll()
                        .requestMatchers("/api/v1/panel/activate").permitAll()

                        .requestMatchers("/api/v1/panel/create").hasRole("HR")

                        .requestMatchers("/api/interview/schedule").hasRole("HR")
                        .requestMatchers("/api/interview/status").hasAnyAuthority("HR", "ROLE_HR")

                        .requestMatchers("/api/interview/feedback/**").hasRole("PANEL")
                        .requestMatchers("/api/interview/panel/**").hasRole("PANEL")

                        .requestMatchers(
                                "/api/candidates/register",
                                "/api/candidates/login")
                        .permitAll()

                        .requestMatchers("/api/interview/candidate/**").hasRole("CANDIDATE")

                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/hr/jd").permitAll()

                        .requestMatchers("/api/hr/**").hasAnyAuthority("HR", "ROLE_HR")
                        .requestMatchers("/api/panel/**").hasAnyAuthority("PANEL", "ROLE_PANEL")
                        .requestMatchers("/api/candidates/onboard").hasAnyAuthority("HR", "ROLE_HR")
                        .requestMatchers("/api/candidates/**").hasAnyAuthority("CANDIDATE", "ROLE_CANDIDATE")

                        .anyRequest().authenticated())

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configures CORS settings.
     *
     * @return CorsConfigurationSource instance
     */
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
     * @return PasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Authentication provider for user authentication.
     *
     * @return AuthenticationProvider instance
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
     * @param config authentication configuration
     * @return AuthenticationManager instance
     * @throws Exception if creation fails
     */
    @Bean
    public AuthenticationManager authenticationManager(
            final AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
