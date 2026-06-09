package org.example.authspringbootsecurity.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationMs;

    @Value("${jwt.refresh.expiration}")
    private int jwtRefreshExpirationMs;

    public String generateAccessToken(String username, String roles) {
        return generateToken(username, roles, jwtExpirationMs);
    }

    public String generateRefreshToken(String username) {
        return generateToken(username, null, jwtRefreshExpirationMs);
    }

    private String generateToken(String username, String roles, int expirationMs) {
        Map<String, Object> claims = new HashMap<>();
        if(roles != null && !roles.isEmpty()) {
            claims.put("roles", roles);
        }

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token);

            return true;
        } catch(MalformedJwtException ex) {
            log.warn("Invalid JWT token format: {}", ex.getMessage());
        } catch(ExpiredJwtException ex) {
            log.warn("JWT token has expired: {}", ex.getMessage());
        } catch(UnsupportedJwtException ex) {
            log.warn("JWT signature validation failed - possible tampering: {}", ex.getMessage());
        } catch(IllegalArgumentException ex) {
            log.warn("JWT token is null or empty: {}", ex.getMessage());
        }
        return false;
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
    }

    public String getRolesFromToken(String token) {
        Claims payload = Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();

        return (String) payload.get("roles");
    }

    // Utility Functions

    public Long getAccessTokenExpirationSeconds() {
        return jwtExpirationMs / 1000L;
    }

    public Long getRefreshTokenExpirationSeconds() {
        return jwtRefreshExpirationMs / 1000L;
    }

    public boolean isTokenExpiringSoon(String token, long thresholdMs) {
        try {
            Claims claims = Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();

            Date expiration = claims.getExpiration();
            long timeUntillExpiration = expiration.getTime() - new Date().getTime();

            return timeUntillExpiration < thresholdMs;
        } catch(ExpiredJwtException ex) {
            return true;
        } catch(Exception ex) {
            log.warn("Could not check token expiration: {}", ex.getMessage());
            return true;
        }
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
