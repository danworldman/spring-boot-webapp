package com.example.webapp.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTaskRequestDto(
        @NotBlank @Size(max = 100)
        String title,
        @NotBlank @Size(max = 2000)
        String text
) {}