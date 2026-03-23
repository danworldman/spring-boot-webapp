package com.example.webapp.dto.task;

public record TaskResponseDto(
        Long id,
        Long userId,
        String title,
        String text
) {}