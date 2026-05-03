package com.interview_tracking_system.backend.security;

/**
 * Static imports for assertions and Mockito methods.
 */
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * Entity and enum imports used for user security testing.
 */
import com.interview_tracking_system.backend.entity.User;
import com.interview_tracking_system.backend.enums.Role;
import com.interview_tracking_system.backend.enums.UserStatus;

/**
 * Repository import used to mock user lookup.
 */
import com.interview_tracking_system.backend.repository.UserRepository;

/**
 * Servlet import used for filter chain testing.
 */
import jakarta.servlet.FilterChain;

/**
 * Java utility import used for optional user result.
 */
import java.util.Optional;

/**
 * JUnit imports used for test methods and Mockito extension.
 */
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Mockito imports used for mock dependencies.
 */
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Spring mock imports used for request and response testing.
 */
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * Spring security imports used for context and user details testing.
 */
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * This class tests JWT utility, custom user details service and JWT filter.
 *
 * It checks token creation, token validation, user loading
 * and security context population.
 */
@ExtendWith(MockitoExtension.class)
class JwtAndUserDetailsTest {

        /**
         * Mocked user repository.
         */
        @Mock
        private UserRepository userRepository;

        /**
         * Mocked servlet filter chain.
         */
        @Mock
        private FilterChain filterChain;

        /**
         * Tests JWT token generation, email extraction, role extraction and validation.
         */
        @Test
        void jwtUtilShouldGenerateExtractAndValidateToken() {
                JwtUtil util = new JwtUtil();

                String token = util.generateAccessToken("raj@test.com", "HR");

                assertEquals("raj@test.com", util.extractEmail(token));
                assertEquals("ROLE_HR", util.extractRole(token));
                assertTrue(util.validateToken(token));
                assertFalse(util.validateToken("invalid.token.value"));
        }

        /**
         * Tests user loading from repository and missing user exception.
         */
        @Test
        void customUserDetailsShouldLoadUserAndThrowWhenMissing() {
                User user = new User();
                user.setEmail("hr@test.com");
                user.setPassword("encoded");
                user.setRole(Role.HR);
                user.setStatus(UserStatus.ACTIVE);

                when(userRepository.findByEmailIgnoreCase("hr@test.com"))
                                .thenReturn(Optional.of(user));

                CustomUserDetailsService service = new CustomUserDetailsService(userRepository);

                UserDetails details = service.loadUserByUsername("hr@test.com");

                assertEquals("hr@test.com", details.getUsername());

                assertTrue(
                                details.getAuthorities()
                                                .stream()
                                                .anyMatch(authority -> authority.getAuthority().equals("ROLE_HR")));

                when(userRepository.findByEmailIgnoreCase("missing@test.com"))
                                .thenReturn(Optional.empty());

                assertThrows(
                                UsernameNotFoundException.class,
                                () -> service.loadUserByUsername("missing@test.com"));
        }

        /**
         * Tests JWT filter with valid bearer token.
         *
         * @throws Exception if filter execution fails
         */
        @Test
        void jwtAuthFilterShouldPopulateSecurityContextForValidBearerToken() throws Exception {
                JwtUtil jwtUtil = spy(new JwtUtil());
                CustomUserDetailsService detailsService = mock(CustomUserDetailsService.class);
                JwtAuthFilter filter = new JwtAuthFilter(jwtUtil, detailsService);

                String token = jwtUtil.generateAccessToken("hr@test.com", "HR");

                UserDetails details = org.springframework.security.core.userdetails.User
                                .withUsername("hr@test.com")
                                .password("x")
                                .roles("HR")
                                .build();

                MockHttpServletRequest request = new MockHttpServletRequest();
                request.addHeader("Authorization", "Bearer " + token);

                MockHttpServletResponse response = new MockHttpServletResponse();

                SecurityContextHolder.clearContext();

                filter.doFilter(request, response, filterChain);

                assertEquals(
                                "hr@test.com",
                                SecurityContextHolder.getContext().getAuthentication().getName());

                verify(filterChain).doFilter(request, response);

                SecurityContextHolder.clearContext();
        }

        /**
         * Tests JWT filter when authorization header is missing.
         *
         * @throws Exception if filter execution fails
         */
        @Test
        void jwtAuthFilterShouldContinueWithoutBearerToken() throws Exception {
                JwtAuthFilter filter = new JwtAuthFilter(
                                new JwtUtil(),
                                mock(CustomUserDetailsService.class));

                MockHttpServletRequest request = new MockHttpServletRequest();
                MockHttpServletResponse response = new MockHttpServletResponse();

                SecurityContextHolder.clearContext();

                filter.doFilter(request, response, filterChain);

                assertNull(SecurityContextHolder.getContext().getAuthentication());

                verify(filterChain).doFilter(request, response);
        }
}
