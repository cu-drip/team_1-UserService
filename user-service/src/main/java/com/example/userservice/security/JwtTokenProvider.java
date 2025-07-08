package com.example.userservice.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider {
    private final String secretFilePath;
    private Key jwtKey;
    private final long jwtExpirationMs = 259200000; // 3 дня

    public JwtTokenProvider(@Value("${jwt.secret-file}") String secretFilePath) {
        this.secretFilePath = secretFilePath;
    }

    @PostConstruct
    private void init() {
        try {
            byte[] secretBytes = Files.readAllBytes(Path.of(secretFilePath));
            this.jwtKey = Keys.hmacShaKeyFor(secretBytes);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось прочитать секретный ключ для JWT", e);
        }
    }


    public String generateToken(UUID userId, boolean isAdmin) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("isAdmin", isAdmin)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(jwtKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public UUID getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return UUID.fromString(claims.getSubject());
    }

    public boolean isAdminFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        Object flag = claims.get("isAdmin");
        if (flag instanceof Boolean) {
            return (Boolean) flag;
        }
        return Boolean.parseBoolean(flag.toString());
    }
}
