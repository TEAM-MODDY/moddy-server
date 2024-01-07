package com.moddy.server.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String jwtSecret;
    private static final String USER_ID = "USER_ID";
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String REFRESH_TOKEN = "REFRESH_TOKEN";
    public static final long DAYS_IN_MILLISECONDS = 24 * 60 * 60 * 1000L;
    public static final int ACCESS_TOKEN_EXPIRATION_DAYS = 30;
    public static final int REFRESH_TOKEN_EXPIRATION_DAYS = 60;

    @PostConstruct
    protected void init() {
        jwtSecret = Base64.getEncoder()
                .encodeToString(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(final String userId) {
        final Claims claims = getAccessTokenClaims();

        claims.put(USER_ID, userId);
        return createToken(claims);
    }

    public String createRefreshToken(final String userId) {
        final Claims claims = getRefreshTokenClaims();

        claims.put(USER_ID, userId);
        return createToken(claims);
    }

    private String createToken(final Claims claims) {
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .signWith(getSigningKey())
                .compact();
    }

    private Claims getRefreshTokenClaims() {
        final Date now = new Date();
        return Jwts.claims()
                .setSubject(REFRESH_TOKEN)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_DAYS * DAYS_IN_MILLISECONDS));
    }

    private Claims getAccessTokenClaims() {
        final Date now = new Date();
        return Jwts.claims()
                .setSubject(ACCESS_TOKEN)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_DAYS * DAYS_IN_MILLISECONDS));
    }

    private Claims getBody(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        final byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
