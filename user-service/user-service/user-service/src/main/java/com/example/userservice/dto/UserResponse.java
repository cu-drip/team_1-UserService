package com.example.userservice.dto;

import com.example.userservice.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class UserResponse {
    private UUID id;
    private String name;
    private String surname;
    private String patronymic;
    private String phoneNumber;
    private String email;
    private String hashedPassword;
    private boolean isAdmin;
    private String dateOfBirth;
    private Integer age;
    private String sex;
    private Float weight;
    private Float height;
    private String createdAt;
    private String bio;
    private String avatarUrl;

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getPatronymic(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getHashedPassword(),
                user.isAdmin(),
                user.getDateOfBirth() != null ? user.getDateOfBirth().toString() : null,
                user.getAge(),
                user.getSex()  != null ? user.getSex().toString() : null,
                user.getWeight(),
                user.getHeight(),
                user.getCreatedAt() != null ? user.getCreatedAt().toString() : null,
                user.getBio(),
                user.getAvatarUrl()
        );
    }
} 