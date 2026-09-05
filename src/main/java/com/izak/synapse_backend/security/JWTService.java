package com.izak.synapse_backend.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JWTService {
    @Value("${app.secret}")
    String appSecret;

    @Value("${app.expiration}")
    int appExpiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(appSecret.getBytes(StandardCharsets.UTF_8));
    }

    public Map<String, String> generateAccessAndRefreshToken(String username) {
        String accessToken = generateToken(username, "access");
        String refreshToken = generateToken(username, "refresh");
        return new HashMap<String, String>() {{
            put("accessToken", accessToken);
            put("refreshToken", refreshToken);
        }};
    }

    public String generateToken(String username) {
        long now = System.currentTimeMillis();
        long expirationTime = System.currentTimeMillis() + (appExpiration * 24 * 60 * 60) * 1000; // Convert days to milliseconds

        return Jwts.builder()
                .subject(username)
                .claim("role", "user")
                .issuedAt(new java.util.Date(now))
                .expiration(new java.util.Date(expirationTime))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateToken(String username, String type) {
        long now = System.currentTimeMillis();
        long expirationTime = System.currentTimeMillis() + (appExpiration * 24 * 60 * 60) * 1000; // Convert days to milliseconds
        if (type.equals("refresh")) {
            expirationTime = System.currentTimeMillis() + ((appExpiration + 7) * 24 * 60 * 60) * 1000; // Convert days to milliseconds
        }

        return Jwts.builder()
                .subject(username)
                .claim("role", "user")
                .claim("type", type)
                .issuedAt(new java.util.Date(now))
                .expiration(new java.util.Date(expirationTime))
                .signWith(getSigningKey())
                .compact();
    }

     public Claims extractPayload(String token){

        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }
     public String extractUsername(String token){

        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

    }


    public boolean isTokenValid(String token, UserDetails user){

        Claims claims = extractPayload(token);
        String username = claims.getSubject();
        boolean isTokenExpired = claims.getExpiration().before(new java.util.Date());

        return username
                .equals(user.getUsername())
                && !isTokenExpired;

    }
}
