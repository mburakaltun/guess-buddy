package com.mburakaltun.guessbuddy.common.util;

import com.mburakaltun.guessbuddy.common.model.enums.AuthorizationRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

@Slf4j
@UtilityClass
public class JwtUtility {
    private final String SECRET_KEY = "zOiVwTl0pF+s7b5B2e4YWrl7HTnPz+xh8+K3XHw9lB0=";
    private final HashMap<AuthorizationRole, Integer> ROLE_EXPIRATION_MAP = new HashMap<>() {{
        put(AuthorizationRole.ROLE_STANDARD_USER, 1);
        put(AuthorizationRole.ROLE_ADMIN, 720);
    }};

    public String generateToken(String email, AuthorizationRole role) {
        Date now = new Date();
        LocalDateTime expirationLocalDateTime = LocalDateTime.now().plusHours(ROLE_EXPIRATION_MAP.get(role));
        Date expirationDate = Date.from(expirationLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());

        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

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

    public static String extractUsername(String token) {
        if (StringUtility.isBlank(token)) {
            return null;
        }

        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    private boolean isTokenExpired(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

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

    private static boolean isUsernameValid(String token, String username) {
        String extractedUsername = extractUsername(token);
        return username.equals(extractedUsername);
    }
}

