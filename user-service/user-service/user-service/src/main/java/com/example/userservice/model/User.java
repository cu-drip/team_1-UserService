package com.example.userservice.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String surname;
    private String patronymic;
    @Column(nullable = false)
    private String email;
    @Column(name = "hashedPassword")
    private String hashedPassword;
    private boolean isAdmin;
    @Column(nullable = true)
    private LocalDate dateOfBirth;
    @Column(nullable = true)
    private Integer age;
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Sex sex;
    @Column(nullable = true)
    private Float weight;
    @Column(nullable = true)
    private Float height;
    private LocalDateTime createdAt;
    @Column(nullable = true)
    private String bio;
    @Column(nullable = true)
    private String avatarUrl;
    @Column(nullable = true)
    private String phoneNumber;
} 