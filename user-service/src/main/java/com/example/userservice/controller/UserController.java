package com.example.userservice.controller;

import com.example.userservice.dto.UserCreateRequest;
import com.example.userservice.dto.UserUpdateRequest;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User API", description = "Операции с пользователями")
public class UserController {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping(path = {"", "/"})
    @Operation(summary = "Получить всех пользователей", description = "Возвращает список всех пользователей.")
    @ApiResponse(responseCode = "200", description = "Список пользователей", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping(path = {"", "/"})
    @Operation(summary = "Создать пользователя", description = "Создаёт нового пользователя.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Пользователь создан", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные", content = @Content)
    })
    public ResponseEntity<User> createUser(@Valid @RequestBody UserCreateRequest request) {
        if (userRepository.findAll().stream().anyMatch(u -> u.getEmail().equals(request.getEmail()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Пользователь с таким email уже существует");
        }
        User user = User.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .patronymic(request.getPatronymic())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .hashedPassword(request.getHashedPassword())
                .isAdmin(request.isAdmin())
                .dateOfBirth(request.getDateOfBirth() != null ? java.time.LocalDate.parse(request.getDateOfBirth()) : null)
                .age(request.getAge())
                .sex(request.getSex() != null ? com.example.userservice.model.Sex.valueOf(request.getSex().toUpperCase()) : null)
                .weight(request.getWeight())
                .height(request.getHeight())
                .bio(request.getBio())
                .avatarUrl(request.getAvatarUrl())
                .createdAt(java.time.LocalDateTime.now())
                .build();
        userRepository.save(user);
        return ResponseEntity.status(201).body(user);
    }

    @GetMapping("/me")
    @Operation(summary = "Получить пользователя по JWT токену", description = "Возвращает пользователя, соответствующего переданному JWT токену.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Пользователь найден", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "401", description = "Невалидный или отсутствует токен", content = @Content),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @Content)
    })
    public ResponseEntity<User> getUserByToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Отсутствует или некорректный Authorization header");
        }
        String token = authHeader.substring(7);
        if (!jwtTokenProvider.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Невалидный JWT токен");
        }
        UUID userId = jwtTokenProvider.getUserIdFromToken(token);
        return userRepository.findById(userId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));
    }

    @PatchMapping("/me")
    @Operation(summary = "Обновить пользователя по JWT токену", description = "Обновляет данные пользователя, соответствующего JWT токену.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Пользователь обновлён", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "401", description = "Невалидный или отсутствует токен", content = @Content),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @Content)
    })
    public ResponseEntity<User> updateUserByToken(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody UserUpdateRequest request) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Отсутствует или некорректный Authorization header");
        }
        String token = authHeader.substring(7);
        if (!jwtTokenProvider.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Невалидный JWT токен");
        }
        UUID userId = jwtTokenProvider.getUserIdFromToken(token);
        return userRepository.findById(userId).map(user -> {
            if (request.getName() != null) user.setName(request.getName());
            if (request.getSurname() != null) user.setSurname(request.getSurname());
            if (request.getPatronymic() != null) user.setPatronymic(request.getPatronymic());
            if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
            if (request.getEmail() != null) user.setEmail(request.getEmail());
            if (request.getDateOfBirth() != null) user.setDateOfBirth(java.time.LocalDate.parse(request.getDateOfBirth()));
            if (request.getAge() != null) user.setAge(request.getAge());
            if (request.getSex() != null)
                user.setSex(com.example.userservice.model.Sex.valueOf(request.getSex().toUpperCase()));
            if (request.getWeight() != null) user.setWeight(request.getWeight());
            if (request.getHeight() != null) user.setHeight(request.getHeight());
            if (request.getBio() != null) user.setBio(request.getBio());
            if (request.getAvatarUrl() != null) user.setAvatarUrl(request.getAvatarUrl());
            userRepository.save(user);
            return ResponseEntity.ok(user);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));
    }

    @DeleteMapping("/me")
    @Operation(summary = "Удалить пользователя по JWT токену", description = "Удаляет пользователя, соответствующего JWT токену.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Пользователь удалён"),
        @ApiResponse(responseCode = "401", description = "Невалидный или отсутствует токен", content = @Content),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @Content)
    })
    public ResponseEntity<Void> deleteUserByToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Отсутствует или некорректный Authorization header");
        }
        String token = authHeader.substring(7);
        if (!jwtTokenProvider.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Невалидный JWT токен");
        }
        UUID userId = jwtTokenProvider.getUserIdFromToken(token);
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        userRepository.deleteById(userId);
        return ResponseEntity.ok().build();
    }
}
