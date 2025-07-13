package com.example.userservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "User Service API",
        version = "v1",
        description = "API для управления пользователями (версия 1)"
    )
)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
} 