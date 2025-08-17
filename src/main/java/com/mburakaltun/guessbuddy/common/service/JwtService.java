package com.mburakaltun.guessbuddy.common.service;

import com.mburakaltun.guessbuddy.common.model.enums.AuthorizationRole;
import com.mburakaltun.guessbuddy.common.properties.JwtProperties;
import com.mburakaltun.guessbuddy.common.util.StringUtility;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;

    public String generateToken(String email, AuthorizationRole role) {
        Date now = new Date();
        int expirationMinutes = role == AuthorizationRole.ROLE_ADMIN ?
                jwtProperties.getAccessToken().getAdminExpirationMinutes() :
                jwtProperties.getAccessToken().getStandardUserExpirationMinutes();

        LocalDateTime expirationLocalDateTime = LocalDateTime.now().plusMinutes(expirationMinutes);
        Date expirationDate = Date.from(expirationLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());

        Key key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token, String username) {
        try {
            return isUsernameValid(token, username) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        if (StringUtility.isBlank(token)) {
            return null;
        }

        Key key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    private boolean isTokenExpired(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isUsernameValid(String token, String username) {
        String extractedUsername = extractUsername(token);
        return username.equals(extractedUsername);
    }

    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    public LocalDateTime calculateRefreshTokenExpiryDate() {
        return LocalDateTime.now().plusDays(jwtProperties.getRefreshToken().getExpirationDays());
    }

    public String generateAccessTokenFromRefreshToken(String email, AuthorizationRole role) {
        return generateToken(email, role);
    }
}