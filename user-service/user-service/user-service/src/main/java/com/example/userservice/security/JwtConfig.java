package com.example.userservice.security;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
public class JwtConfig {
    @Bean
    public SecretKey jwtKey(@Value("${jwt.secret-file}") String path) throws IOException {
        return Keys.hmacShaKeyFor(Files.readAllBytes(Path.of(path)));
    }
} 