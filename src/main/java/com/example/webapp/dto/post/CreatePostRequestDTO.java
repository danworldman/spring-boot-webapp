package com.example.webapp.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePostRequestDTO(
        @NotBlank(message = "Контент поста не может быть пустым")
        @Size(min = 5, max = 500, message = "Пост должен быть от 5 до 500 символов")
        String content
) {}