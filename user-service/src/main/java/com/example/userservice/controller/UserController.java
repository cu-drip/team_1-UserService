package com.example.userservice.controller;

import com.example.userservice.dto.UserCreateRequest;
import com.example.userservice.dto.UserUpdateRequest;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(path = {"", "/"})
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<User> createUser(@RequestBody UserCreateRequest request) {
        User user = User.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .patronymic(request.getPatronymic())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .hashedPassword(request.getHashedPassword())
                .isAdmin(request.isAdmin())
                .dateOfBirth(request.getDateOfBirth() != null ? LocalDate.parse(request.getDateOfBirth()) : null)
                .age(request.getAge())
                .sex(request.getSex() != null ? com.example.userservice.model.Sex.valueOf(request.getSex().toUpperCase()) : null)
                .weight(request.getWeight())
                .height(request.getHeight())
                .bio(request.getBio())
                .avatarUrl(request.getAvatarUrl())
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(user);
        return ResponseEntity.status(201).body(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody UserUpdateRequest request) {
        return userRepository.findById(id).map(user -> {
            if (request.getName() != null) user.setName(request.getName());
            if (request.getSurname() != null) user.setSurname(request.getSurname());
            if (request.getPatronymic() != null) user.setPatronymic(request.getPatronymic());
            if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
            if (request.getEmail() != null) user.setEmail(request.getEmail());
            if (request.getDateOfBirth() != null) user.setDateOfBirth(LocalDate.parse(request.getDateOfBirth()));
            if (request.getAge() != null) user.setAge(request.getAge());
            if (request.getSex() != null)
                user.setSex(com.example.userservice.model.Sex.valueOf(request.getSex().toUpperCase()));
            if (request.getWeight() != null) user.setWeight(request.getWeight());
            if (request.getHeight() != null) user.setHeight(request.getHeight());
            if (request.getBio() != null) user.setBio(request.getBio());
            if (request.getAvatarUrl() != null) user.setAvatarUrl(request.getAvatarUrl());
            userRepository.save(user);
            return ResponseEntity.ok(user);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
} 