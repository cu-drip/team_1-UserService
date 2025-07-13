package com.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {
    private String name;
    private String surname;
    private String patronymic;
    private String email;
    private String hashedPassword; // Оставляем для создания пользователей
    private boolean isAdmin;
    private String phoneNumber;
    private String dateOfBirth;
    private Integer age;
    private String sex;
    private Float weight;
    private Float height;
    private String bio;
    private String avatarUrl;
    private Integer mmr; // Опциональное поле mmr
} 