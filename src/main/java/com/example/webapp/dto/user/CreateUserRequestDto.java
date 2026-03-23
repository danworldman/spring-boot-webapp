package com.example.webapp.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequestDto(
        @NotBlank(message = "Имя не может быть пустым")
        @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов")
        String name,

        @NotBlank(message = "Email обязателен")
        @Email(message = "Неверный формат email")
        String email,

        // поля SECURITY:
        @NotBlank(message = "Логин обязателен")
        @Size(min = 3, max = 20, message = "Логин должен быть от 3 до 20 символов")
        String username,

        @NotBlank(message = "Пароль обязателен")
        @Size(min = 4, message = "Пароль должен быть не менее 4 символов")
        String password
) {}