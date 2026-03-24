package com.example.webapp.dto.task;

public record TaskResponseDTO(
        Long id,
        Long userId,
        String title,
        String text
) {}