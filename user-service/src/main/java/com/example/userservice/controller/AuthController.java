package com.example.userservice.controller;

import com.example.userservice.dto.*;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.security.JwtTokenProvider;
import com.example.userservice.service.AuthService;
import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    @Autowired
    public AuthController(UserService userService, JwtTokenProvider jwtTokenProvider, AuthService authService, UserRepository userRepository) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            String result = authService.login(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(new LoginResponse(result));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }
        String token = authHeader.substring(7);
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(401).build();
        }
        UUID userId = jwtTokenProvider.getUserIdFromToken(token);
        return userRepository.findById(userId)
                .map(user -> {
                    UserResponse resp = UserResponse.from(user);
                    return ResponseEntity.ok(resp);
                })
                .orElse(ResponseEntity.status(401).build());
    }

    @PatchMapping("/me")
    public ResponseEntity<UserResponse> updateMe(@RequestHeader("Authorization") String authHeader, @RequestBody UserUpdateRequest request) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }
        String token = authHeader.substring(7);
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(401).build();
        }
        UUID userId = jwtTokenProvider.getUserIdFromToken(token);
        return userRepository.findById(userId).map(user -> {
            if (request.getName() != null) user.setName(request.getName());
            if (request.getSurname() != null) user.setSurname(request.getSurname());
            if (request.getPatronymic() != null) user.setPatronymic(request.getPatronymic());
            if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
            if (request.getEmail() != null) user.setEmail(request.getEmail());
            if (request.getDateOfBirth() != null) user.setDateOfBirth(LocalDate.parse(request.getDateOfBirth()));
            if (request.getAge() != null) user.setAge(request.getAge());
            if (request.getSex() != null) user.setSex(com.example.userservice.model.Sex.valueOf(request.getSex().toUpperCase()));
            if (request.getWeight() != null) user.setWeight(request.getWeight());
            if (request.getHeight() != null) user.setHeight(request.getHeight());
            if (request.getBio() != null) user.setBio(request.getBio());
            if (request.getAvatarUrl() != null) user.setAvatarUrl(request.getAvatarUrl());
            userRepository.save(user);
            return ResponseEntity.ok(UserResponse.from(user));
        }).orElseGet(() -> ResponseEntity.status(401).build());
    }
} 