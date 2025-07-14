package com.example.userservice.dto;

import com.example.userservice.model.Sex;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String name;
    private String surname;
    private String patronymic;
    private String email;
    // Убрано поле hashedPassword из ответов API
    // private String hashedPassword;
    private boolean isAdmin;
    private LocalDate dateOfBirth;
    private Integer age;
    private Sex sex;
    private Float weight;
    private Float height;
    private LocalDateTime createdAt;
    private String bio;
    private String avatarUrl;
    private String phoneNumber;
    private Integer mmr;

    public static UserResponse fromUser(com.example.userservice.model.User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getPatronymic(),
                user.getEmail(),
                // Убрано поле hashedPassword из ответов API
                // user.getHashedPassword(),
                user.isAdmin(),
                user.getDateOfBirth(),
                user.getAge(),
                user.getSex(),
                user.getWeight(),
                user.getHeight(),
                user.getCreatedAt(),
                user.getBio(),
                user.getAvatarUrl(),
                user.getPhoneNumber(),
                user.getMmr()
        );
    }
} 