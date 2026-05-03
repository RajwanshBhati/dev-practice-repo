package com.interview_tracking_system.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.springframework.stereotype.Component;

/**
 * Utility class for JWT operations.
 */
@Component
public final class JwtUtil {

    /**
     * JWT secret key.
     */
    private static final String SECRET_KEY = "my-super-secret-key-my-super-secret-key";

    /**
     * Access token expiration time.
     */
    private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 15;

    /**
     * Returns signing key.
     *
     * @return signing key
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates access token.
     *
     * @param email user email
     * @param role  user role
     * @return access token
     */
    public String generateAccessToken(final String email, final String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", "ROLE_" + role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extracts email from token.
     *
     * @param token JWT token
     * @return email
     */
    public String extractEmail(final String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Extracts role from token.
     *
     * @param token JWT token
     * @return role
     */
    public String extractRole(final String token) {
        return getClaims(token).get("role", String.class);
    }

    /**
     * Validates token.
     *
     * @param token JWT token
     * @return true if token is valid
     */
    public boolean validateToken(final String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Extracts claims.
     *
     * @param token JWT token
     * @return claims
     */
    private Claims getClaims(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
