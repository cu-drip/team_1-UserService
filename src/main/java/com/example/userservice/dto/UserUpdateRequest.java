package com.example.userservice.dto;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String name;
    private String surname;
    private String patronymic;
    private String phoneNumber;
    private String email;
    private String dateOfBirth;
    private Integer age;
    private String sex;
    private Float weight;
    private Float height;
    private String bio;
    private String avatarUrl;
    private Integer mmr; // Опциональное поле mmr
} 