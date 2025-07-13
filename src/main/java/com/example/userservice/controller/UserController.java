package com.example.userservice.controller;

import com.example.userservice.dto.UserCreateRequest;
import com.example.userservice.dto.UserUpdateRequest;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.dto.UserResponse;
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

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(path = {"", "/"})
    @Operation(summary = "Получить всех пользователей", description = "Возвращает список всех пользователей.")
    @ApiResponse(responseCode = "200", description = "Список пользователей", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class)))
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::fromUser)
                .toList();
    }

    @PostMapping(path = {"", "/"})
    @Operation(summary = "Создать пользователя", description = "Создаёт нового пользователя.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Пользователь создан", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "400", description = "Некорректные данные", content = @Content)
    })
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
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
                .mmr(request.getMmr() != null ? request.getMmr() : 100) // Дефолтное значение 100
                .createdAt(java.time.LocalDateTime.now())
                .build();
        userRepository.save(user);
        return ResponseEntity.status(201).body(UserResponse.fromUser(user));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID", description = "Возвращает пользователя по его идентификатору.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Пользователь найден", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @Content)
    })
    public ResponseEntity<UserResponse> getUserById(@PathVariable java.util.UUID id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(UserResponse.fromUser(user)))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновить пользователя", description = "Обновляет данные пользователя по его идентификатору.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Пользователь обновлён", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @Content)
    })
    public ResponseEntity<UserResponse> updateUser(@PathVariable java.util.UUID id, @Valid @RequestBody UserUpdateRequest request) {
        return userRepository.findById(id).map(user -> {
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
            if (request.getMmr() != null) user.setMmr(request.getMmr()); // Добавлена поддержка обновления mmr
            userRepository.save(user);
            return ResponseEntity.ok(UserResponse.fromUser(user));
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя по его идентификатору.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Пользователь удалён"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<Void> deleteUser(@PathVariable java.util.UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
} 