package com.example.userservice.controller;

import com.example.userservice.dto.*;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.security.JwtTokenProvider;
import com.example.userservice.service.AuthService;
import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.UUID;
import com.example.userservice.dto.ChangePasswordRequest;
import com.example.userservice.model.User;

@RestController
@RequestMapping("/api/v1/auth")
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
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(authService.register(request));
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            String result = authService.login(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(new LoginResponse(result));
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);
        if (!jwtTokenProvider.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
        java.util.UUID userId = jwtTokenProvider.getUserIdFromToken(token);
        return userRepository.findById(userId)
                .map(user -> ResponseEntity.ok(UserResponse.from(user)))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found or unauthorized"));
    }

    @PatchMapping("/me")
    public ResponseEntity<UserResponse> updateMe(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody UserUpdateRequest request) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);
        if (!jwtTokenProvider.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
        java.util.UUID userId = jwtTokenProvider.getUserIdFromToken(token);
        return userRepository.findById(userId).map(user -> {
            if (request.getName() != null) user.setName(request.getName());
            if (request.getSurname() != null) user.setSurname(request.getSurname());
            if (request.getPatronymic() != null) user.setPatronymic(request.getPatronymic());
            if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
            if (request.getEmail() != null) user.setEmail(request.getEmail());
            if (request.getDateOfBirth() != null) user.setDateOfBirth(java.time.LocalDate.parse(request.getDateOfBirth()));
            if (request.getAge() != null) user.setAge(request.getAge());
            if (request.getSex() != null) user.setSex(com.example.userservice.model.Sex.valueOf(request.getSex().toUpperCase()));
            if (request.getWeight() != null) user.setWeight(request.getWeight());
            if (request.getHeight() != null) user.setHeight(request.getHeight());
            if (request.getBio() != null) user.setBio(request.getBio());
            if (request.getAvatarUrl() != null) user.setAvatarUrl(request.getAvatarUrl());
            userRepository.save(user);
            return ResponseEntity.ok(UserResponse.from(user));
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found or unauthorized"));
    }

    @PatchMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody ChangePasswordRequest request) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);
        if (!jwtTokenProvider.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
        java.util.UUID userId = jwtTokenProvider.getUserIdFromToken(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found or unauthorized"));
        if (!new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().matches(request.getOldPassword(), user.getHashedPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Старый пароль неверен");
        }
        user.setHashedPassword(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(request.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }
} 