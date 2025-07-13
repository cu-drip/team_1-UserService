package com.example.userservice.service;

import com.example.userservice.dto.RegisterRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public UserResponse register(RegisterRequest request) {
        // Проверка, что email не занят
        userRepository.findByEmail(request.getEmail()).ifPresent(u -> {
            throw new RuntimeException("Email already in use");
        });
        // Хэширование пароля
        String passwordHash = passwordEncoder.encode(request.getPassword());
        // Создание пользователя
        User user = User.builder()
                .email(request.getEmail())
                .hashedPassword(passwordHash)
                .name(request.getName())
                .surname(request.getSurname())
                .patronymic(request.getPatronymic())
                .phoneNumber(request.getPhoneNumber())
                .isAdmin(true)
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(user);
        // Формируем ответ
        return UserResponse.from(user);
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(password, user.getHashedPassword())) {
            throw new RuntimeException("Invalid password");
        }
        // Генерируем JWT-токен с ролями
        java.util.List<String> roles = new java.util.ArrayList<>();
        if (user.isAdmin()) {
            roles.add("ROLE_ADMIN");
        } else {
            roles.add("ROLE_USER");
        }
        return jwtTokenProvider.generateToken(user.getId(), user.getEmail(), roles);
    }
} 
